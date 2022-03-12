package com.example.frequency.screen.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.frequency.MainActivity
import com.example.frequency.R
import com.example.frequency.databinding.FragmentProfileBinding
import com.example.frequency.foundation.contract.ProvidesCustomActions
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.contract.navigator
import com.example.frequency.foundation.views.BaseFragment
import com.example.frequency.utils.ALERT
import com.example.frequency.utils.ActionStore.menuAction
import com.example.frequency.utils.ERROR
import com.example.frequency.utils.UtilPermission.ALL_PERMISSIONS
import com.example.frequency.utils.UtilPermission.hasPermissions
import com.example.frequency.utils.dialog_fragment.SimpleDialogFragment
import com.example.frequency.utils.setUserImageByGlide
import com.example.frequency.utils.showSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment(), ProvidesCustomActions, ProvidesCustomTitle {

    override val viewModel by viewModels<ProfileVM>()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val currentUser get() = viewModel.user.value

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
        ::onGotPermissionResult
    )

    private val pickImageLauncher = registerForActivityResult(OpenDocument(), ::onReferenceReceived)

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

    private val startSettingActivityIntent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    )

    private fun askForOpeningSettings() {
        if (context?.packageManager?.resolveActivity(
                startSettingActivityIntent, PackageManager.MATCH_DEFAULT_ONLY
            ) == null
        ) {
            showSnackbar(binding.root, "Permission denied forever!", iconPreset = ERROR)
        } else {
            SimpleDialogFragment.show(
                parentFragmentManager,
                "Application work incorrectly.",
                "Our app will not works without this permission, you can add it in settings."
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        // initialise listeners

        binding.includedProfileInfo.userIcon.setOnClickListener {
            val user = currentUser
            user?.let {
                if (it.icon != Uri.EMPTY) {
                    navigator().openSettings()
                } else {
                    permissionLauncher.launch(ALL_PERMISSIONS)
                }
            }
        }

        viewModel.user.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            binding.includedProfileInfo.userNameTv.text = it.name
            binding.includedProfileInfo.userEmailTv.text = it.email
            setUserImageByGlide(requireContext(), binding.includedProfileInfo.userIcon, it.icon)
            binding.includedProfileInfo.central.isVisible = true
            navigator().provideResult(it)
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setSimpleDialogFragmentListener()
    }

    private fun setListeners() {
        binding.logoutTb.setOnClickListener {
            viewModel.clearUserRootPreferences()
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    triggerRebirth(requireContext())
                }, 1000
            )
        }

    }

    private fun setSimpleDialogFragmentListener() {
        SimpleDialogFragment.setUpListener(parentFragmentManager, viewLifecycleOwner) {
            startSettingActivityIntent
        }
    }

    private fun triggerRebirth(context: Context) {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        if (context is Activity) {
            context.finish()
        }
        Runtime.getRuntime().exit(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getTitleRes() = R.string.profile

    override fun getCustomActions() = listOf(menuAction)


    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }
}