package me.melijn.dumbhome.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import me.melijn.dumbhome.R
import me.melijn.dumbhome.components.Location
import me.melijn.dumbhome.database.Database
import me.melijn.dumbhome.databinding.FragmentHomeBinding
import me.melijn.dumbhome.objects.ItemClickListener

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        val homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding.viewModel = homeViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = HomeAdapter(ItemClickListener(locationClickListener = { locationItem ->
            println("such clickiness " + locationItem.location.toString())
        }))


        val manager = GridLayoutManager(activity, 2)
        manager.offsetChildrenVertical(0)

        binding.locationList.layoutManager = manager
        binding.locationList.adapter = adapter

        val homeItemList = ArrayList<HomeItem.LocationItem>()
        val locationItemParams = HashMap<Location, Int>()
        Database.switches.value?.forEach {
            val amount = locationItemParams.getOrElse(it.location, { 0 })
            locationItemParams[it.location] = amount + 1
        }

        for ((id, entry) in locationItemParams.entries.withIndex()) {
            homeItemList.add(HomeItem.LocationItem(id, entry.key, entry.value))
        }

        adapter.submitList(homeItemList.toList())
        return binding.root
    }
}