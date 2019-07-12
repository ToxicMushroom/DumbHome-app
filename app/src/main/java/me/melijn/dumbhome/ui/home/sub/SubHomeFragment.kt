package me.melijn.dumbhome.ui.home.sub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import me.melijn.dumbhome.MainActivity
import me.melijn.dumbhome.R
import me.melijn.dumbhome.components.toLocation
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
        binding.viewModel = subHomeViewModel
        binding.lifecycleOwner = this
        val mainActivity = activity as MainActivity?

        val adapter = SubHomeAdapter(ItemClickListener())


        val manager = LinearLayoutManager(activity)
        binding.deviceHomeList.layoutManager = manager
        //binding.deviceHomeList.adapter = adapter


        val switchItemList = Database.switches.value?.filter {
            it.location == arguments.locationName.toLocation()
        }?.map { DHItem.SwitchItem(it, false) }

        println(switchItemList?.joinToString())
        //adapter.submitList(switchItemList)
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> findNavController().navigate(SubHomeFragmentDirections.actionSubHomeFragmentToNavigationHome())

            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}