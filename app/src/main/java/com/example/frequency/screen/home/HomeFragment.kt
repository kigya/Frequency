package com.example.frequency.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.frequency.R
import com.example.frequency.databinding.FragmentHomeBinding
import com.example.frequency.foundation.contract.ProvidesCustomActions
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.contract.navigator
import com.example.frequency.foundation.model.CustomAction
import com.example.frequency.foundation.model.MenuAction
import com.example.frequency.foundation.views.BaseFragment
import com.google.android.material.snackbar.Snackbar

class HomeFragment : BaseFragment(), ProvidesCustomTitle, ProvidesCustomActions {

    override val viewModel by viewModels<HomeVM>()

    private var _binding: FragmentHomeBinding? = null
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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        // initialise listeners

        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun getTitleRes() = R.string.home

    override fun getCustomActions() = listOf(
        MenuAction(
            iconRes = R.drawable.ic_navigation_menu,
            textRes = R.string.menu,
            onCustomAction = { navigator().run { showSnackbar("menu") } }
        ),
        CustomAction(
            iconRes = R.drawable.ic_unknown_user_photo,
            textRes = R.string.user,
            onCustomAction = { navigator().run { showSnackbar("user") } }
        )
    )

    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        snackbar.view.setOnClickListener {
            snackbar.dismiss()
        }
        snackbar.show()
    }


}