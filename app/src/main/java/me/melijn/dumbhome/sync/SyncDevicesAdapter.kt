package me.melijn.dumbhome.sync

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.melijn.dumbhome.components.SwitchComponent
import me.melijn.dumbhome.databinding.ListItemSyncSubmitBinding
import me.melijn.dumbhome.databinding.ListItemSyncSwitchBinding
import me.melijn.dumbhome.objects.ItemClickListener

const val ITEM_VIEW_TYPE_SWITCH = 0
const val ITEM_VIEW_TYPE_BUTTON = 1
const val ITEM_VIEW_TYPE_FOOTER = 9

class SyncDevicesAdapter(val clickListener: ItemClickListener) :
    ListAdapter<DHSyncItem, RecyclerView.ViewHolder>(DHItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            ITEM_VIEW_TYPE_SWITCH -> SwitchViewHolder.from(parent)
            //ITEM_VIEW_TYPE_BUTTON -> ButtonViewHolder.from(parent)

            ITEM_VIEW_TYPE_FOOTER -> FooterViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SwitchViewHolder -> {
                val switchItem = getItem(position) as DHSyncItem.SwitchItem
                holder.bind(switchItem, clickListener)
            }
            is FooterViewHolder -> {
                val footerItem = getItem(position) as DHSyncItem.FooterItem
                holder.bind(footerItem, clickListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DHSyncItem.SwitchItem -> ITEM_VIEW_TYPE_SWITCH
            is DHSyncItem.FooterItem -> ITEM_VIEW_TYPE_FOOTER
        }
    }

    class SwitchViewHolder private constructor(val binding: ListItemSyncSwitchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DHSyncItem.SwitchItem, clickListener: ItemClickListener) {
            binding.switchItem = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): SwitchViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSyncSwitchBinding.inflate(layoutInflater, parent, false)
                return SwitchViewHolder(binding)
            }
        }
    }

    class FooterViewHolder private constructor(val binding: ListItemSyncSubmitBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(footerItem: DHSyncItem.FooterItem, clickListener: ItemClickListener) {
            binding.footerItem = footerItem
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): FooterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSyncSubmitBinding.inflate(layoutInflater, parent, false)
                return FooterViewHolder(binding)
            }
        }
    }
}


class DHItemDiffCallback : DiffUtil.ItemCallback<DHSyncItem>() {
    override fun areItemsTheSame(oldItem: DHSyncItem, newItem: DHSyncItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: DHSyncItem, newItem: DHSyncItem): Boolean {
        return oldItem == newItem
    }
}

sealed class DHSyncItem {
    abstract val id: Int

    data class SwitchItem(val switchComponent: SwitchComponent, var currentState: Boolean = false) :
        DHSyncItem() {
        override val id: Int = ITEM_VIEW_TYPE_SWITCH * MAX_ITEMS_PER_TYPE + switchComponent.id
    }

    data class FooterItem(override val id: Int) : DHSyncItem()
}