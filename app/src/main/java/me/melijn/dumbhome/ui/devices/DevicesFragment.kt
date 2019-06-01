package me.melijn.dumbhome.ui.devices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import me.melijn.dumbhome.R
import me.melijn.dumbhome.databinding.FragmentDevicesBinding

class DevicesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentDevicesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_devices, container, false)
        val dashboardViewModel = ViewModelProviders.of(this).get(DevicesViewModel::class.java)
        binding.viewModel = dashboardViewModel
        binding.setLifecycleOwner { lifecycle }

        return binding.root
    }
}