package com.example.frequency.presentation.ui.screens.info.profile

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.frequency.R
import com.example.frequency.databinding.FragmentProfileBinding
import com.example.frequency.foundation.contract.ProvidesCustomActions
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.contract.navigator
import com.example.frequency.foundation.views.BaseFragment
import com.example.frequency.common.utils.ActionStore.menuAction
import com.example.frequency.common.utils.SummaryUtils.ALERT
import com.example.frequency.common.utils.SummaryUtils.ERROR
import com.example.frequency.common.utils.SummaryUtils.showSnackbar
import com.example.frequency.common.utils.UtilPermission.ALL_PERMISSIONS
import com.example.frequency.common.utils.UtilPermission.hasPermissions
import com.example.frequency.common.utils.dialog_fragment.SimpleDialogFragment
import com.example.frequency.common.utils.dialog_fragment.SimpleDialogFragment.Companion.NEGATIVE_FRAG_RESPONSE
import com.example.frequency.common.utils.dialog_fragment.SimpleDialogFragment.Companion.NEUTRAL_FRAG_RESPONSE
import com.example.frequency.common.utils.dialog_fragment.SimpleDialogFragment.Companion.POSITIVE_FRAG_RESPONSE
import com.example.frequency.common.utils.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment(), ProvidesCustomActions, ProvidesCustomTitle {


    override val viewModel by viewModels<ProfileVM>()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val currentUser get() = viewModel.user.value

    private val settingsLauncher =
        registerForActivityResult(StartActivityForResult(), ::onSettingsResult)

    private val permissionLauncher = registerForActivityResult(
        RequestMultiplePermissions(),
        ::onGotPermissionResult
    )
    private val pickImageLauncher = registerForActivityResult(OpenDocument(), ::onReferenceReceived)

    private lateinit var startSettingActivityIntent: Intent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        // initialise listeners

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initiateListener()
        initiateObserver()
        setSimpleDialogFragmentListener()
    }

    private fun initiateListener() {
        with(binding) {
            includedProfileInfo.userIcon.setOnClickListener {
                val user = currentUser
                user?.let {
                    if (it.icon != Uri.EMPTY) {
                        navigator().openPreview(it.icon.toString())
                        showSnackbar(binding.root, "Long Click to set image.")
                    } else {
                        permissionLauncher.launch(ALL_PERMISSIONS)
                    }
                }
            }
            includedProfileInfo.userIcon.setOnLongClickListener {
                val user = currentUser
                user?.let {
                    permissionLauncher.launch(ALL_PERMISSIONS)
                }
                return@setOnLongClickListener true
            }

        }

    }

    private fun initiateObserver() {
        viewModel.user.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            binding.includedProfileInfo.userNameTv.text = it.name
            binding.includedProfileInfo.userEmailTv.text = it.email
            setImageByGlide(requireContext(), binding.includedProfileInfo.userIcon, it.icon, 100)
            binding.includedProfileInfo.central.isVisible = true
            navigator().provideResult(it)
        }
        viewModel.showPbLd.observeEvent(viewLifecycleOwner) {
            navigator().showProgress(it)
        }

    }

    private fun onReferenceReceived(uri: Uri?) {
        uri?.let {
            context?.contentResolver?.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            viewModel.setUpdatedImage(uri)
        }
    }

    private fun onGotPermissionResult(results: Map<String, Boolean>) {
        if (hasPermissions(requireContext(), *results.keys.toTypedArray())) {
            pickImageLauncher.launch(arrayOf("image/*"))
        } else { //false
            if (!shouldShowRequestPermissionRationale(results.keys.last())) {
                askForOpeningSettings()
            } else {
                showSnackbar(binding.root, "Permission needed!", iconPreset = ALERT)
            }
        }
    }

    private fun onSettingsResult(result: ActivityResult) {
        Log.d(TAG, result.toString())
        permissionLauncher.launch(ALL_PERMISSIONS)
    }

    private fun setSimpleDialogFragmentListener() {
        SimpleDialogFragment.setUpListener(parentFragmentManager, viewLifecycleOwner) { response ->
            when (response) {
                POSITIVE_FRAG_RESPONSE -> settingsLauncher.launch(startSettingActivityIntent)
                NEGATIVE_FRAG_RESPONSE -> {}
                NEUTRAL_FRAG_RESPONSE -> {}
            }
        }
    }

    private fun askForOpeningSettings() {
        startSettingActivityIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context?.packageName, null)
        )

        if (context?.packageManager?.resolveActivity(
                startSettingActivityIntent, PackageManager.MATCH_DEFAULT_ONLY
            ) == null
        ) {
            showSnackbar(binding.root, "Permission denied forever!", iconPreset = ERROR)
        } else {
            SimpleDialogFragment.show(
                fragmentManager = parentFragmentManager,
                icon = R.drawable.ic_crisis_alert,
                title = R.string.incorrect_app_work,
                message = R.string.works_without_perm,
                posBut = R.string.settings_tb,
                neuBut = R.string.close_tb,
            )
        }
    }

    private fun setImageByGlide(
        requireContext: Context,
        view: ImageView,
        uri: Uri?,
        timeout: Int = 0
    ) {
        Glide.with(requireContext)
            .load(uri)
            .timeout(timeout)
            .error(R.drawable.ic_unknown_user_photo)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getTitleRes() = R.string.profile

    override fun getCustomActions() = listOf(menuAction)


    companion object {

        @JvmStatic
        private val TAG = ProfileFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = ProfileFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }
}