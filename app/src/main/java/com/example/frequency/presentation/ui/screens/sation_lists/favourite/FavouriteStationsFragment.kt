package com.example.frequency.presentation.ui.screens.sation_lists.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import com.example.frequency.R
import com.example.frequency.common.utils.ActionStore.menuAction
import com.example.frequency.common.utils.ActionStore.provideProfileAction
import com.example.frequency.data.remote.toStationsList
import com.example.frequency.databinding.FragmentFavouriteBinding
import com.example.frequency.foundation.contract.ProvidesCustomActions
import com.example.frequency.foundation.contract.ProvidesCustomTitle
import com.example.frequency.foundation.contract.navigator
import com.example.frequency.foundation.model.Action
import com.example.frequency.foundation.model.state.UIState
import com.example.frequency.foundation.views.BaseFragment
import com.example.frequency.presentation.common.StationsRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteStationsFragment : BaseFragment(), ProvidesCustomTitle, ProvidesCustomActions {

    override val viewModel by viewModels<FavouriteStationsVM>()

    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var stationsAdapter: StationsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeListeners()
        initializeObservers()
    }

    private fun initializeListeners() {

    }

    private fun initializeObservers() {
        viewModel.uiState.asLiveData().observe(viewLifecycleOwner) { state ->
            navigator().showProgress(false)
            when (state) {
                is UIState.Empty -> {

                }
                is UIState.Pending -> {
                    navigator().showProgress(true)
                }
                is UIState.Error -> {
                    with(state.error) {

                    }
                }
                is UIState.Success -> {
                    with(state.data) {
                        stationsAdapter =
                            StationsRecyclerAdapter(stationList.toStationsList()) { station ->
                                navigator().openStation(station)
                            }
                        binding.radioStationListRw.adapter = stationsAdapter
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        @JvmStatic
        private val TAG = "FavouriteStationsFragment"

        fun newInstance() = FavouriteStationsFragment().apply {
            arguments = Bundle().apply {

            }
        }

    }

    override fun getCustomActions() = listOf(
        menuAction,
        provideProfileAction { navigator().openProfile() }
    )

    override fun getTitleRes() = R.string.favourite_stations

}