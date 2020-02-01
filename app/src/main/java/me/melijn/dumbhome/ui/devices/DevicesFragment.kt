package me.melijn.dumbhome.ui.devices

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
import me.melijn.dumbhome.database.Database
import me.melijn.dumbhome.databinding.FragmentDevicesBinding
import me.melijn.dumbhome.objects.ItemClickListener
import me.melijn.dumbhome.ui.sync.ITEM_VIEW_TYPE_SWITCH

class DevicesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentDevicesBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_devices, container, false)
        val dashboardViewModel = ViewModelProvider(this).get(DevicesViewModel::class.java)

        binding.viewModel = dashboardViewModel
        binding.lifecycleOwner = this

        val adapter = DevicesAdapter(ItemClickListener(devicesClickListener = { deviceItem ->
            this.findNavController().navigate(
                DevicesFragmentDirections.actionNavigationDevicesToSubDevicesFragment(
                    deviceItem.toDeviceName(),
                    deviceItem.id
                )
            )
        }))

        val manager = GridLayoutManager(activity, 2)

        binding.deviceList.layoutManager = manager
        binding.deviceList.adapter = adapter

        val deviceItemList = ArrayList<DeviceItem.DeviceItemImpl>()
        val switchAmount = Database.switches.value?.size
        switchAmount?.let {
            deviceItemList.add(
                DeviceItem.DeviceItemImpl(
                    ITEM_VIEW_TYPE_SWITCH,
                    it
                )
            )
        }

        adapter.submitList(deviceItemList.toList())

        dashboardViewModel.initValues(context)
        return binding.root
    }
}