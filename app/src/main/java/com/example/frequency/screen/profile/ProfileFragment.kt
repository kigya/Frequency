package com.example.frequency.screen.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.frequency.MainActivity
import com.example.frequency.R
import com.example.frequency.databinding.FragmentProfileBinding
import com.example.frequency.foundation.contract.ProvidesCustomActions
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.contract.navigator
import com.example.frequency.foundation.views.BaseFragment
import com.example.frequency.screen.song.SongFragment
import com.example.frequency.utils.ActionStore.menuAction
import com.example.frequency.utils.setUserImageByGlide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment(), ProvidesCustomActions, ProvidesCustomTitle {

    override val viewModel by viewModels<ProfileVM>()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialise all data
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        // initialise listeners

        binding.includedProfileInfo.userIcon.setOnClickListener {
            navigator().openSettings()
        }

        viewModel.user.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            binding.includedProfileInfo.userNameTv.text = it.name
            binding.includedProfileInfo.userEmailTv.text = it.email
            setUserImageByGlide(requireContext(), binding.includedProfileInfo.userIcon, it.icon)
            binding.includedProfileInfo.central.isVisible = true
        }

        setListeners()

        return binding.root
    }

    private fun setListeners(){
        binding.logoutTb.setOnClickListener {
            viewModel.clearUserRootPreferences()
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    triggerRebirth(requireContext())
                }, 1000
            )
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

    companion object {
        @JvmStatic
        fun newInstance() = SongFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

    override fun getTitleRes() = R.string.profile

    override fun getCustomActions() = listOf(menuAction)
}