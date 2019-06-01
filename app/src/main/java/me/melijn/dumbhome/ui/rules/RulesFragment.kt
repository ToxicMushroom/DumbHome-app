package me.melijn.dumbhome.ui.rules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import me.melijn.dumbhome.R
import me.melijn.dumbhome.databinding.FragmentDevicesBinding
import me.melijn.dumbhome.databinding.FragmentRulesBinding
import me.melijn.dumbhome.ui.devices.DevicesViewModel

class RulesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentRulesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_rules, container, false)
        val rulesViewModel = ViewModelProviders.of(this).get(RulesViewModel::class.java)
        binding.viewModel = rulesViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}