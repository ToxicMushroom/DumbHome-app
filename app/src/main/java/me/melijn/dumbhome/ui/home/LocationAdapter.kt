package me.melijn.dumbhome.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.melijn.dumbhome.ItemClickListener
import me.melijn.dumbhome.components.Location
import me.melijn.dumbhome.databinding.ListItemLocationBinding

class LocationAdapter() :
    ListAdapter<HomeItem.LocationItem, RecyclerView.ViewHolder>(LocationItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("FIX")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

class LocationItemDiffCallback : DiffUtil.ItemCallback<HomeItem.LocationItem>() {
    override fun areItemsTheSame(
        oldItem: HomeItem.LocationItem,
        newItem: HomeItem.LocationItem
    ): Boolean {
        return oldItem.location == newItem.location
    }

    override fun areContentsTheSame(
        oldItem: HomeItem.LocationItem,
        newItem: HomeItem.LocationItem
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