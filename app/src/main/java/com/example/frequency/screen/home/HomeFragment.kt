package com.example.frequency.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.frequency.R
import com.example.frequency.foundation.contract.navigator
import com.example.frequency.databinding.FragmentHomeBinding
import com.example.frequency.foundation.contract.ProvidesCustomActions
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.views.BaseFragment
import com.example.frequency.preferences.AppDefaultPreferences
import com.example.frequency.utils.ActionStore.menuAction
import com.example.frequency.utils.ActionStore.provideProfileAction
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment(), ProvidesCustomTitle, ProvidesCustomActions {

    override val viewModel by viewModels<HomeVM>()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPreferences: AppDefaultPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialise all data
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        // initialise listeners

        binding.helloTextview.text =
            getString(R.string.hello_username, sharedPreferences.getUsername())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fooLD.observe(viewLifecycleOwner) {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun getTitleRes() = R.string.home

    override fun getCustomActions() = listOf(
        menuAction,
        provideProfileAction { navigator().openProfile() }
    )

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

}