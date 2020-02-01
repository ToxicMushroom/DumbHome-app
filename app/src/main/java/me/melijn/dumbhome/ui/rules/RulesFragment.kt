package me.melijn.dumbhome.ui.rules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import me.melijn.dumbhome.R
import me.melijn.dumbhome.databinding.FragmentRulesBinding

class RulesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentRulesBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_rules, container, false)
        val rulesViewModel = ViewModelProvider(this).get(RulesViewModel::class.java)
        binding.viewModel = rulesViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}