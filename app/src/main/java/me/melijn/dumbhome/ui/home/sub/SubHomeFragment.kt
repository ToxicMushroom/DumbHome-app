package me.melijn.dumbhome.ui.home.sub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import me.melijn.dumbhome.R
import me.melijn.dumbhome.components.toLocation
import me.melijn.dumbhome.database.Database
import me.melijn.dumbhome.databinding.FragmentSubHomeBinding
import me.melijn.dumbhome.objects.ItemClickListener
import me.melijn.dumbhome.sync.ITEM_VIEW_TYPE_SWITCH
import me.melijn.dumbhome.sync.MAX_ITEMS_PER_TYPE


class SubHomeFragment : Fragment() {

    private var switchItemList: List<DHItem.SwitchItem> = ArrayList()

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

        switchItemList = Database.switches.value?.filter {
            it.location == arguments.locationName.toLocation()
        }?.map { DHItem.SwitchItem(it) }!!

        binding.viewModel = subHomeViewModel
        binding.lifecycleOwner = this

        binding.deviceHomeList.layoutManager = manager
        binding.deviceHomeList.adapter = adapter

        switchItemList[0].state.value = true
        adapter.submitList(switchItemList)
        activity?.applicationContext?.let {
            Database().refreshSwitchStates(
                PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext).all,
                it
            )
        }
        Database.switches.observe(this, Observer { array ->
            for (switchComponent in array) {
                val switchItem =
                    switchItemList.find { switchItem -> switchItem.id == MAX_ITEMS_PER_TYPE * ITEM_VIEW_TYPE_SWITCH + switchComponent.id }
                switchItem?.let {
                    switchItemList[switchItemList.indexOf(it)].state.value = switchComponent.isOn
                }
            }
        })

        return binding.root
    }

}