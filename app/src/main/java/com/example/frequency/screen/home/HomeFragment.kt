package com.example.frequency.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frequency.R
import com.example.frequency.databinding.FragmentHomeBinding
import com.example.frequency.foundation.contract.ProvidesCustomActions
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.contract.navigator
import com.example.frequency.foundation.views.BaseFragment
import com.example.frequency.services.radio_browser.StationsRecyclerAdapter
import com.example.frequency.services.radio_browser.radostation_list.toStation
import com.example.frequency.utils.ActionStore.menuAction
import com.example.frequency.utils.ActionStore.provideProfileAction
import com.example.frequency.utils.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment(), ProvidesCustomTitle, ProvidesCustomActions {

    override val viewModel by viewModels<HomeVM>()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var stationsAdapter: StationsRecyclerAdapter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.radioStationListRw.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        lifecycle.addObserver(viewModel)


        initiateListeners()
        initiateObservers()
    }

    private fun initiateListeners() {
        with(binding){
            nextOffsetBt.setOnClickListener {
                viewModel.loadStation()
            }
            previousOffsetBt.setOnClickListener {
                viewModel.loadStation(direction = false)
            }

        }
    }

    private fun initiateObservers() {
        with(viewModel) {
            viewModel.userLD.observe(viewLifecycleOwner) {
                binding.helloTextview.text = getString(R.string.hello_username, it.name)
            }
            showPbLd.observeEvent(viewLifecycleOwner) {
                navigator().showProgress(it)
            }
            stationListLD.observe(viewLifecycleOwner) { stationList ->
                stationsAdapter = StationsRecyclerAdapter(stationList.toStation()) { station ->
                    navigator().openSong(station)
                }
                binding.radioStationListRw.adapter = stationsAdapter
            }


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