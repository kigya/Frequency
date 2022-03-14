package com.example.frequency.screen.song

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.frequency.R
import com.example.frequency.databinding.FragmentSongBinding
import com.example.frequency.foundation.contract.ProvidesCustomActions
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.contract.navigator
import com.example.frequency.foundation.views.BaseFragment
import com.example.frequency.network.radio_browser.models.Station
import com.example.frequency.utils.ActionStore.menuAction
import com.example.frequency.utils.ActionStore.provideProfileAction
import com.example.frequency.utils.SummaryUtils.ERROR
import com.example.frequency.utils.SummaryUtils.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException


@AndroidEntryPoint
class SongFragment : BaseFragment(), ProvidesCustomTitle, ProvidesCustomActions {

    override val viewModel by viewModels<SongVM>()

    private var _binding: FragmentSongBinding? = null
    private val binding get() = _binding!!

    private var station: Station? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialise all data
        station = requireArguments().getParcelable(ARG_STATION)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSongBinding.inflate(inflater, container, false)
        // initialise listeners

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(viewModel)

        if (station != null) {
            viewModel.updateStation(station)
        }

        initializeListeners()
        initializeObservers()

    }

    private fun initializeObservers() {
        viewModel.stationLD.observe(viewLifecycleOwner) {
            updateUI(station ?: throw IllegalStateException("Where is my station"))
        }
    }

    private fun initializeListeners() {
        with(binding) {
            buttonPlay.setOnClickListener {
                startPlaying()
            }
        }

    }

    private fun startPlaying() {

        val player = MediaPlayer()
        try {
            player.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            viewModel.stationLD.value?.url?.let {
                player.setDataSource(requireContext(), Uri.parse(it))
            }
            player.prepare()
            player.start()

        } catch (e: IOException) {
            Log.d(TAG, e.message.toString())
            showSnackbar(binding.root, e.message.toString(), ERROR)
        }


    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(station: Station) {
        with(binding) {
            Glide.with(root.context)
                .load(station.favicon)
                .error(R.drawable.ic_audiotrack_24)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .timeout(0)
                .into(binding.previewIv)
            stationNameTv.text = station.name
            //nowPlayingTv.text = station. "1%$skb, 1%$s, 1%$s, 1%$s"
            shortDescriptionTv.text =
                "${station.bitrate}kb, ${station.codec}, ${station.country}, ${station.state}"
            additionalTv.text =
                station.tags?.replace(Regex(","), ", ")
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getTitleRes() = R.string.station

    override fun getCustomActions() = listOf(
        menuAction, provideProfileAction { navigator().openProfile() }
    )

    companion object {
        @JvmStatic
        fun newInstance(station: Station) = SongFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_STATION, station)
            }
        }

        @JvmStatic
        private val TAG = SongFragment::class.java.simpleName

        @JvmStatic
        private val ARG_STATION = "ARG_STATION"
    }

}