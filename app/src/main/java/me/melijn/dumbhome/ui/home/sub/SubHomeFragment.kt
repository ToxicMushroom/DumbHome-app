package me.melijn.dumbhome.ui.home.sub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import me.melijn.dumbhome.R
import me.melijn.dumbhome.database.Database
import me.melijn.dumbhome.databinding.FragmentSubHomeBinding
import me.melijn.dumbhome.objects.ItemClickListener
import me.melijn.dumbhome.sync.DHItem

class SubHomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSubHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sub_home, container, false)
        val subHomeViewModel = ViewModelProviders.of(this).get(SubHomeViewModel::class.java)
        val arguments = SubHomeFragmentArgs.fromBundle(arguments!!)
        activity?.actionBar?.title = arguments.location.toString()
        binding.viewModel = subHomeViewModel
        binding.lifecycleOwner = this
        activity?.title = arguments.location.toString()
        val adapter = SubHomeAdapter(ItemClickListener())


        val manager = LinearLayoutManager(activity)
        binding.deviceHomeList.layoutManager = manager
        //binding.deviceHomeList.adapter = adapter


        val switchItemList = Database.switches.value?.filter {
            it.location == arguments.location
        }?.map { DHItem.SwitchItem(it, false) }

        println(switchItemList?.joinToString())
        //adapter.submitList(switchItemList)
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                activity?.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}