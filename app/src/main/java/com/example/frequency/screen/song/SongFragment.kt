package com.example.frequency.screen.song

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
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
import com.example.frequency.services.FrequencyRadioService
import com.example.frequency.services.MusicState
import com.example.frequency.utils.ActionStore.menuAction
import com.example.frequency.utils.ActionStore.provideProfileAction
import com.example.frequency.utils.requireEvent
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SongFragment : BaseFragment(), ProvidesCustomTitle, ProvidesCustomActions {

    override val viewModel by viewModels<SongVM>()

    private var _binding: FragmentSongBinding? = null
    private val binding get() = _binding!!

    private var station: Station? = null

    private var frequencyRadioService: FrequencyRadioService? = null

    private val boundServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder: FrequencyRadioService.StationBinder =
                service as FrequencyRadioService.StationBinder
            frequencyRadioService = binder.getService()
            viewModel.setFrequencyServiceBound(true)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            frequencyRadioService?.runAction(MusicState.STOP)
            frequencyRadioService = null
            viewModel.setFrequencyServiceBound(false)
        }
    }

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

    private fun initializeListeners() {
        with(binding) {
            buttonPlay.setOnClickListener {
                sendCommandToBoundService(MusicState.PLAY)
                startPlaying()
            }
        }

    }

    private fun initializeObservers() {
        viewModel.stationLD.observe(viewLifecycleOwner) {
            updateUI(station ?: throw IllegalStateException("Where is my station"))
        }
    }

    override fun onStart() {
        super.onStart()
        if (!viewModel.isMusicServiceBound.requireEvent()) {
            bindToFrequencyRadioService()
        }


    }

    private fun sendCommandToBoundService(state: MusicState) {
        frequencyRadioService?.runAction(state)
    }

    private fun bindToFrequencyRadioService() {
        Intent(requireContext(), FrequencyRadioService::class.java).also {
            requireActivity().bindService(it, boundServiceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun unbindFrequencyService() {
        frequencyRadioService?.runAction(MusicState.STOP)
        requireActivity().unbindService(boundServiceConnection)
    }

    private fun startPlaying() {

    }

    private fun stopPlaying(){

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

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindFrequencyService()
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