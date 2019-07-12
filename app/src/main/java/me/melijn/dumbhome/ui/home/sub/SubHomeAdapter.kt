package me.melijn.dumbhome.ui.home.sub

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.melijn.dumbhome.components.SwitchComponent
import me.melijn.dumbhome.databinding.ListItemSubHomeSwitchBinding
import me.melijn.dumbhome.objects.ItemClickListener
import me.melijn.dumbhome.sync.ITEM_VIEW_TYPE_SWITCH
import me.melijn.dumbhome.sync.MAX_ITEMS_PER_TYPE

class SubHomeAdapter(val clickListener: ItemClickListener) :
    ListAdapter<DHItem, RecyclerView.ViewHolder>(SubHomeItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_SWITCH -> SwitchViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SwitchViewHolder -> {
                val switchItem = getItem(position) as DHItem.SwitchItem
                holder.bind(switchItem, clickListener)
            }
        }
    }

    class SwitchViewHolder private constructor(val binding: ListItemSubHomeSwitchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DHItem.SwitchItem, clickListener: ItemClickListener) {
            binding.switchItem = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): SwitchViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSubHomeSwitchBinding.inflate(layoutInflater, parent, false)
                return SwitchViewHolder(binding)
            }
        }
    }

}

class SubHomeItemDiffCallback : DiffUtil.ItemCallback<DHItem>() {
    override fun areItemsTheSame(
        oldItem: DHItem,
        newItem: DHItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: DHItem,
        newItem: DHItem
    ): Boolean {
        return oldItem == newItem
    }
}

sealed class DHItem {
    abstract val id: Int

    data class SwitchItem(val switchComponent: SwitchComponent) : DHItem() {
        override val id: Int = ITEM_VIEW_TYPE_SWITCH * MAX_ITEMS_PER_TYPE + switchComponent.id
    }
}
