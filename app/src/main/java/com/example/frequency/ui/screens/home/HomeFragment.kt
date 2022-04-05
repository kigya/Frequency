package com.example.frequency.ui.screens.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frequency.R
import com.example.frequency.databinding.FragmentHomeBinding
import com.example.frequency.datasource.network.radio_browser.StationsRecyclerAdapter
import com.example.frequency.datasource.network.radio_browser.TagsRecyclerAdapter
import com.example.frequency.datasource.network.radio_browser.radostation_list.toStationsList
import com.example.frequency.foundation.contract.ProvidesCustomActions
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.contract.navigator
import com.example.frequency.foundation.views.BaseFragment
import com.example.frequency.ui.screens.home.home_state_data.HomeUIState
import com.example.frequency.utils.ActionStore.menuAction
import com.example.frequency.utils.ActionStore.provideProfileAction
import com.example.frequency.utils.dialog_fragment.SimpleDialogFragment
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
        setSimpleDialogFragmentListener()
    }

    private fun initiateListeners() {
        with(binding) {
            nextOffsetBt.setOnClickListener {
                viewModel.loadStation()
                viewModel.riseOffset()
            }
            previousOffsetBt.setOnClickListener {
                viewModel.loadStation()
                viewModel.decreaseOffset()
            }
            musicSearch.setOnQueryTextListener(object : OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.dropOffset()
                    viewModel.setUpdatedQuery((query ?: "").trim())
                    viewModel.loadStation()
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    viewModel.setUpdatedQuery((query ?: "").trim())
                    return true
                }
            })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initiateObservers() {
        with(viewModel) {
            uiState.asLiveData().observe(viewLifecycleOwner) { state ->
                navigator().showProgress(false)
                when (state) {
                    is HomeUIState.Empty -> {

                    }
                    is HomeUIState.Pending -> {
                        navigator().showProgress(true)
                    }
                    is HomeUIState.Error -> {
                        with(state.data) {
                            showSimpleDialogFragment(icon, title, message, positive, neutral, negative)
                        }
                    }
                    is HomeUIState.Loaded -> {
                        with(state.data) {
                            tagsAdapter = TagsRecyclerAdapter(tagList) {
                                it.let {
                                    setUpdatedQuery(it)
                                    changeTag(it)
                                    dropOffset()
                                    loadStation()
                                }
                            }
                            binding.tagsAdapter.adapter = tagsAdapter
                            stationsAdapter =
                                StationsRecyclerAdapter(stationList.toStationsList()) { station ->
                                    navigator().openSong(station)
                                }
                            binding.radioStationListRw.adapter = stationsAdapter
                            binding.currentOffset.text =
                                "${currentOffset + stationList.toStationsList().size}"
                        }
                    }
                }
            }
        }
    }

    private fun showSimpleDialogFragment(
        @DrawableRes icon: Int,
        @StringRes title: Int,
        @StringRes message: Int,
        @StringRes positive: Int,
        @StringRes neutral: Int,
        @StringRes negative: Int,
    ) {
        SimpleDialogFragment.show(
            parentFragmentManager,
            icon = icon,
            title = title,
            message = message,
            posBut = positive,
            neuBut = neutral,
            negBut = negative,
        )
    }

    private fun setSimpleDialogFragmentListener() {
        SimpleDialogFragment.setUpListener(parentFragmentManager, viewLifecycleOwner) { response ->
            when (response) {
                SimpleDialogFragment.POSITIVE_FRAG_RESPONSE -> {
                    viewModel.loadStation()
                }
                SimpleDialogFragment.NEGATIVE_FRAG_RESPONSE -> {}
                SimpleDialogFragment.NEUTRAL_FRAG_RESPONSE -> {}
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