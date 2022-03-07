package com.example.frequency.screen.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.frequency.R
import com.example.frequency.databinding.FragmentProfileBinding
import com.example.frequency.foundation.contract.ProvidesCustomActions
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.contract.navigator
import com.example.frequency.foundation.views.BaseFragment
import com.example.frequency.screen.song.SongFragment
import com.example.frequency.utils.menuAction
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

        binding.userIcon.setOnClickListener {
            navigator().openSettings()
        }

        viewModel.user.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            binding.userNameTv.text = it.name
            binding.userEmailTv.text = it.email
            binding.userIcon.setImageURI(it.icon)
        }

        return binding.root
    }

    override fun getTitleRes() = R.string.lyrics

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

    override fun getCustomActions() = listOf(menuAction)
}