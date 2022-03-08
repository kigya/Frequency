package com.example.frequency.screen.song

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.frequency.R
import com.example.frequency.databinding.FragmentLyricsBinding
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.views.BaseFragment

class SongFragment : BaseFragment(), ProvidesCustomTitle {

    override val viewModel by viewModels<SongVM>()

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

    override fun getTitleRes() = R.string.song

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

}