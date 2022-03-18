package com.example.frequency.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frequency.R
import com.example.frequency.databinding.FragmentHomeBinding
import com.example.frequency.foundation.contract.ProvidesCustomActions
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.contract.navigator
import com.example.frequency.foundation.views.BaseFragment
import com.example.frequency.network.radio_browser.StationsRecyclerAdapter
import com.example.frequency.network.radio_browser.TagsRecyclerAdapter
import com.example.frequency.network.radio_browser.radostation_list.toStation
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
    private lateinit var tagsAdapter: TagsRecyclerAdapter

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
        with(binding) {
            nextOffsetBt.setOnClickListener {
                viewModel.riseOffset()
                viewModel.loadStation()
            }
            previousOffsetBt.setOnClickListener {
                viewModel.decreaseOffset()
                viewModel.loadStation()
            }
            musicSearch.setOnQueryTextListener(object : OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (!query.isNullOrBlank()) {
                        viewModel.dropOffset()
                        viewModel.setUpdatedQuery(query.trim())
                        viewModel.loadStation()
                    }
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    viewModel.setUpdatedQuery(query?.trim())
                    return true
                }
            })


        }
    }

    private fun initiateObservers() {
        with(viewModel) {
            showPbLd.observeEvent(viewLifecycleOwner) {
                navigator().showProgress(it)
            }

            stationListLD.observe(viewLifecycleOwner) { stationList ->
                stationsAdapter = StationsRecyclerAdapter(stationList.toStation()) { station ->
                    navigator().openSong(station)
                }
                binding.radioStationListRw.adapter = stationsAdapter
            }

            tagListLD.observe(viewLifecycleOwner) { tags ->
                tagsAdapter = TagsRecyclerAdapter(tags) {
                    it.let {
                        setUpdatedQuery(it)
                        changeTag(it)
                        dropOffset()
                        loadStation()
                    }
                }
                binding.tagsAdapter.adapter = tagsAdapter
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