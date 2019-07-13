package me.melijn.dumbhome.ui.home.sub

import android.os.Bundle
import android.os.Handler
import android.util.SparseLongArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.getOrDefault
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


    private val mInterval = 1000L //millis
    private val networkCooldown = 5000L //millis
    private var mHandler: Handler? = null
    private var preferenceMap: Map<String, Any?>? = null
    private var onCooldown = SparseLongArray()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSubHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sub_home, container, false)

        val subHomeViewModel = ViewModelProviders.of(this).get(SubHomeViewModel::class.java)

        val arguments = SubHomeFragmentArgs.fromBundle(arguments!!)

        val adapter = SubHomeAdapter(ItemClickListener(subHomeClickListener = { switchItem ->
            onCooldown.append(switchItem.id, System.currentTimeMillis())
        }))

        val manager = LinearLayoutManager(activity)

        binding.viewModel = subHomeViewModel
        binding.lifecycleOwner = this

        binding.deviceHomeList.layoutManager = manager
        binding.deviceHomeList.adapter = adapter

        subHomeViewModel.switchItemList = Database.switches.value?.filter {
            it.location == arguments.locationName.toLocation()
        }?.map { DHItem.SwitchItem(it) }!!

        Database.switches.observe(this, Observer { array ->
            for (switchComponent in array.iterator()) {
                val index =
                    subHomeViewModel.switchItemList.indexOfFirst { item -> item.id == MAX_ITEMS_PER_TYPE * ITEM_VIEW_TYPE_SWITCH + switchComponent.id }
                if (index != -1 && subHomeViewModel.switchItemList[index].state.value != switchComponent.isOn) {//check if needs update and index isn't non existing

                    if (onCooldown.getOrDefault(
                            subHomeViewModel.switchItemList[index].id,
                            0
                        ) > System.currentTimeMillis() - networkCooldown
                    ) return@Observer //Still on cooldown

                    subHomeViewModel.switchItemList[index].state.value = switchComponent.isOn
                    adapter.notifyItemChanged(index)
                }
            }
        })

        adapter.submitList(subHomeViewModel.switchItemList)

        preferenceMap =
            PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext).all
        mHandler = Handler()
        startRepeatingTask()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRepeatingTask()
    }

    private val mStatusChecker: Runnable = object : Runnable {
        override fun run() {
            try {
                activity?.applicationContext?.let { context ->
                    preferenceMap?.let { map ->
                        Database().refreshSwitchStates(
                            map,
                            context
                        )
                    }
                }
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler?.postDelayed(this, mInterval)
            }
        }
    }

    private fun startRepeatingTask() {
        mStatusChecker.run()
    }

    private fun stopRepeatingTask() {
        mHandler?.removeCallbacks(mStatusChecker)
    }

}