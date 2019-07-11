package me.melijn.dumbhome.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.melijn.dumbhome.components.Location
import me.melijn.dumbhome.databinding.ListItemLocationBinding
import me.melijn.dumbhome.objects.ItemClickListener


private const val ITEM_VIEW_TYPE_LOCATION = 0

class HomeAdapter(val clickListener: ItemClickListener) :
    ListAdapter<HomeItem, RecyclerView.ViewHolder>(LocationItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_LOCATION -> LocationViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LocationViewHolder -> {
                val locationItem = getItem(position) as HomeItem.LocationItem
                holder.bind(locationItem, clickListener)
            }
        }
    }

    class LocationViewHolder private constructor(val binding: ListItemLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HomeItem.LocationItem, clickListener: ItemClickListener) {
            binding.locationItem = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): LocationViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemLocationBinding.inflate(layoutInflater, parent, false)
                return LocationViewHolder(binding)
            }
        }
    }

}

class LocationItemDiffCallback : DiffUtil.ItemCallback<HomeItem>() {
    override fun areItemsTheSame(
        oldItem: HomeItem,
        newItem: HomeItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: HomeItem,
        newItem: HomeItem
    ): Boolean {
        return oldItem == newItem
    }

}

sealed class HomeItem {
    abstract val id: Int

    data class LocationItem(override val id: Int, val location: Location, val amountDevices: Int) :
        HomeItem()

    fun convertAmountDevices(number: Int): String =
        if (number > 1) "$number devices" else "$number device"
}