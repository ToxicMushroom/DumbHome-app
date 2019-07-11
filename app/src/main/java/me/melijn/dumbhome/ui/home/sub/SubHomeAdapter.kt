package me.melijn.dumbhome.ui.home.sub

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.melijn.dumbhome.objects.ItemClickListener
import me.melijn.dumbhome.sync.DHItem

class SubHomeAdapter(clickListener: ItemClickListener) :
    ListAdapter<DHItem, RecyclerView.ViewHolder>(SubHomeItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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


