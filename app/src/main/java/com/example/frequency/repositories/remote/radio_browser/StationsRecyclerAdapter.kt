package com.example.frequency.repositories.remote.radio_browser

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.frequency.R
import com.example.frequency.databinding.RvItemStationBinding
import com.example.frequency.repositories.remote.radio_browser.models.Station
import com.example.frequency.repositories.remote.radio_browser.radostation_list.Stations

class StationsRecyclerAdapter(
    private val stations: Stations,
    private val chosenItem: (Station) -> Unit
) :
    RecyclerView.Adapter<StationsRecyclerAdapter.StationViewHolder>() {

    private lateinit var binding: RvItemStationBinding

    // create one element of list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        binding = RvItemStationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return StationViewHolder(binding, chosenItem)
    }

    // Take element created in "onCreateViewHolder" and fill this element
    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        holder.setStations(stations[position])
    }

    // return quantity elements of list
    override fun getItemCount(): Int = stations.size

    // Stations ViewHolder - is a container for view & element also
    class StationViewHolder(
        private val binding: RvItemStationBinding,
        val chosenItem: (Station) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private var station: Station? = null

        init {
            binding.root.setOnClickListener {
                station?.let {
                    chosenItem(it)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun setStations(station: Station) {
            this.station = station
            with(binding) {
                stationTitle.text = station.name
                tags.text = station.tags
                shortDescription.text = "${station.bitrate}kb, ${station.country}, ${station.state}"
            }
            if (hasValue(station.favicon)) {

                Glide.with(binding.root.context)
                    .load(station.favicon)
                    .placeholder(R.drawable.ic_audiotrack_24)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .timeout(0)
                    .into(binding.rvStationImage)
            }

        }

        private fun hasValue(value: String?): Boolean {
            return !value.isNullOrBlank()
        }

    }
}