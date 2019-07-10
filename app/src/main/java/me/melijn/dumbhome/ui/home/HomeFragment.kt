package me.melijn.dumbhome.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import me.melijn.dumbhome.R
import me.melijn.dumbhome.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        val homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding.viewModel = homeViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        homeViewModel.initValues(requireContext())
        return binding.root
    }
}