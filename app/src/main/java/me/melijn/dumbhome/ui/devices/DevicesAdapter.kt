package me.melijn.dumbhome.ui.devices

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.melijn.dumbhome.databinding.ListItemDeviceBinding
import me.melijn.dumbhome.objects.ItemClickListener
import me.melijn.dumbhome.ui.sync.ITEM_VIEW_TYPE_BUTTON
import me.melijn.dumbhome.ui.sync.ITEM_VIEW_TYPE_SWITCH

class DevicesAdapter(val clickListener: ItemClickListener) :
    ListAdapter<DeviceItem, RecyclerView.ViewHolder>(DevicesItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_SWITCH -> SwitchDevicesViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SwitchDevicesViewHolder -> {
                val locationItem = getItem(position) as DeviceItem.DeviceItemImpl
                holder.bind(locationItem, clickListener)
            }
        }
    }

    class SwitchDevicesViewHolder private constructor(val binding: ListItemDeviceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DeviceItem.DeviceItemImpl, clickListener: ItemClickListener) {
            binding.deviceItem = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): SwitchDevicesViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemDeviceBinding.inflate(layoutInflater, parent, false)
                return SwitchDevicesViewHolder(binding)
            }
        }
    }

}

class DevicesItemDiffCallback : DiffUtil.ItemCallback<DeviceItem>() {
    override fun areItemsTheSame(
        oldItem: DeviceItem,
        newItem: DeviceItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: DeviceItem,
        newItem: DeviceItem
    ): Boolean {
        return oldItem == newItem
    }

}

sealed class DeviceItem {
    abstract val id: Int

    data class DeviceItemImpl(override val id: Int, val amountDevices: Int) :
        DeviceItem()

    fun convertAmountDevices(number: Int): String =
        if (number > 1) "$number devices" else "$number device"

    fun toDeviceName(): String = when (id) {
        ITEM_VIEW_TYPE_SWITCH -> "Socket switches"
        ITEM_VIEW_TYPE_BUTTON -> "Buttons"
        else -> "unknown type"
    }
}