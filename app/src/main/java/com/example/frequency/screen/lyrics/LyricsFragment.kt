package com.example.frequency.screen.lyrics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.frequency.R
import com.example.frequency.databinding.FragmentHomeBinding
import com.example.frequency.databinding.FragmentLyricsBinding
import com.example.frequency.databinding.ProfileMenuLayoutBinding
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.views.BaseFragment
import com.example.frequency.screen.home.HomeFragment
import com.example.frequency.screen.home.HomeVM

class LyricsFragment : BaseFragment(), ProvidesCustomTitle {

    override val viewModel by viewModels<LyricsVM>()

    private var _binding: FragmentLyricsBinding? = null
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
        _binding = FragmentLyricsBinding.inflate(inflater, container, false)
        // initialise listeners

        return binding.root
    }
    override fun getTitleRes() = R.string.lyrics

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = LyricsFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

}