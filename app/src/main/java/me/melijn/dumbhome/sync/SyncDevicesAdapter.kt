package me.melijn.dumbhome.sync

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.melijn.dumbhome.ItemClickListener
import me.melijn.dumbhome.components.SwitchComponent
import me.melijn.dumbhome.databinding.ListItemSwitchBinding

private const val ITEM_VIEW_TYPE_SWITCH = 0
private const val ITEM_VIEW_TYPE_BUTTON = 1

class SyncDevicesAdapter : ListAdapter<DHItem, RecyclerView.ViewHolder>(DHItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_SWITCH -> SwitchViewHolder.from(parent)
            //ITEM_VIEW_TYPE_BUTTON -> ButtonViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class SwitchViewHolder private constructor(val binding: ListItemSwitchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DHItem.SwitchItem, clickListener: ItemClickListener) {
            binding.switchItem = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): SwitchViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSwitchBinding.inflate(layoutInflater, parent, false)
                return SwitchViewHolder(binding)
            }
        }
    }


}


class DHItemDiffCallback : DiffUtil.ItemCallback<DHItem>() {
    override fun areItemsTheSame(oldItem: DHItem, newItem: DHItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: DHItem, newItem: DHItem): Boolean {
        return oldItem == newItem
    }
}

sealed class DHItem {
    abstract val id: Int

    data class SwitchItem(val switchComponent: SwitchComponent) : DHItem() {
        override val id: Int = switchComponent.id * 10 + ITEM_VIEW_TYPE_SWITCH
    }
}