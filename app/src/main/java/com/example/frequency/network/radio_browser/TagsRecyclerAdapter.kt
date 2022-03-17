package com.example.frequency.network.radio_browser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frequency.databinding.TagAdapterItemBinding

class TagsRecyclerAdapter(
    private val tags: List<String>,
    private val chosenItem: (String) -> Unit
) :
    RecyclerView.Adapter<TagsRecyclerAdapter.TagsViewHolder>() {

    private lateinit var binding: TagAdapterItemBinding

    // create one element of list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TagsViewHolder {
        binding = TagAdapterItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TagsViewHolder(binding, chosenItem)
    }

    // Take element created in "onCreateViewHolder" and fill this element
    override fun onBindViewHolder(
        holder: TagsViewHolder,
        position: Int
    ) {
        holder.setTags(tags[position])
    }

    // return quantity elements of list
    override fun getItemCount(): Int = tags.size

    // Stations ViewHolder - is a container for view & element also
    class TagsViewHolder(
        private val binding: TagAdapterItemBinding,
        val chosenItem: (String) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private var tag: String? = null

        init {
            binding.root.setOnClickListener {
                tag?.let {
                    chosenItem(it)
                }
            }
        }

        fun setTags(tag: String) {
            this.tag = tag
            binding.tagTv.text = tag
        }

    }
}