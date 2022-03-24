package com.example.frequency.ui.screens.info.contact_us

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.frequency.BuildConfig
import com.example.frequency.R
import com.example.frequency.databinding.FragmentContactUsBinding
import com.example.frequency.foundation.contract.ProvidesCustomActions
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.contract.navigator
import com.example.frequency.foundation.views.BaseFragment
import com.example.frequency.utils.ActionStore.menuAction
import com.example.frequency.utils.ActionStore.provideProfileAction
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactUsFragment : BaseFragment(), ProvidesCustomTitle, ProvidesCustomActions {

    override val viewModel by viewModels<ContactUsVM>()

    private var _binding: FragmentContactUsBinding? = null
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
        _binding = FragmentContactUsBinding.inflate(inflater, container, false)
        // initialise listeners

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.aboutVersion.text = getString(R.string.about_version, BuildConfig.VERSION_NAME)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun getTitleRes() = R.string.contact_us

    override fun getCustomActions() = listOf(
        menuAction,
        provideProfileAction { navigator().openProfile() }
    )

    companion object {
        @JvmStatic
        fun newInstance() = ContactUsFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

}