package com.example.frequency.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.frequency.databinding.FragmentHomeBinding
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.views.BaseFragment

class HomeFragment: BaseFragment(), ProvidesCustomTitle{

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

    override fun getTitleRes(): Int {
        TODO("Not yet implemented")
    }

}