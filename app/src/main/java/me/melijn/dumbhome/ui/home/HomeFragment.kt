package me.melijn.dumbhome.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding.viewModel = homeViewModel
        binding.lifecycleOwner = this

        val adapter = HomeAdapter(ItemClickListener(locationClickListener = { locationItem ->
            this.findNavController()
                .navigate(HomeFragmentDirections.actionNavigationHomeToSubHomeFragment(locationItem.location.toName()))
        }))


        val manager = GridLayoutManager(activity, 2)

        binding.locationList.layoutManager = manager
        binding.locationList.adapter = adapter

        val homeItemList = ArrayList<HomeItem.LocationItem>()
        val locationItemParams = HashMap<Location, Int>()
        Database.switches.value
            ?.sortedBy { switch -> switch.name }
            ?.forEach {
                val amount = locationItemParams.getOrElse(it.location, { 0 })
                locationItemParams[it.location] = amount + 1
            }

        for ((id, entry) in locationItemParams.entries.withIndex()) {
            homeItemList.add(HomeItem.LocationItem(id, entry.key, entry.value))
        }

        homeItemList.sortBy { homeItem -> homeItem.location }

        adapter.submitList(homeItemList.toList())
        return binding.root
    }
}