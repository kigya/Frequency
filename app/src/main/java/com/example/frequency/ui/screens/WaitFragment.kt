package com.example.frequency.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.frequency.R
import com.example.frequency.databinding.FragmentWaitBinding
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.views.AuthFragments
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WaitFragment : Fragment(), AuthFragments, ProvidesCustomTitle {

    private var _binding: FragmentWaitBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWaitBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getTitleRes() = R.string.tech_issue
}