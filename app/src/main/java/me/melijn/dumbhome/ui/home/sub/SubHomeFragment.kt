package me.melijn.dumbhome.ui.home.sub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import me.melijn.dumbhome.R
import me.melijn.dumbhome.components.toLocation
import me.melijn.dumbhome.database.Database
import me.melijn.dumbhome.databinding.FragmentSubHomeBinding
import me.melijn.dumbhome.objects.ItemClickListener
import me.melijn.dumbhome.sync.DHSyncItem


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

        val adapter = SubHomeAdapter(ItemClickListener())

        val manager = LinearLayoutManager(activity)

        val switchItemList = Database.switches.value?.filter {
            it.location == arguments.locationName.toLocation()
        }?.map { DHSyncItem.SwitchItem(it, false) }

        binding.viewModel = subHomeViewModel
        binding.lifecycleOwner = this

        binding.deviceHomeList.layoutManager = manager
        binding.deviceHomeList.adapter = adapter

        adapter.submitList(switchItemList)
        activity?.applicationContext?.let {
            Database().refreshSwitchStates(
                PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext).all,
                it
            )
        }

        return binding.root
    }

}