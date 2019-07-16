package me.melijn.dumbhome.ui.devices.sub

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
import me.melijn.dumbhome.database.Database
import me.melijn.dumbhome.databinding.FragmentSubDevicesBinding
import me.melijn.dumbhome.io.StateRepository
import me.melijn.dumbhome.objects.ItemClickListener
import me.melijn.dumbhome.ui.home.sub.DHItem
import me.melijn.dumbhome.ui.home.sub.SubHomeAdapter
import me.melijn.dumbhome.ui.sync.ITEM_VIEW_TYPE_SWITCH
import me.melijn.dumbhome.ui.sync.MAX_ITEMS_PER_TYPE


class SubDevicesFragment : Fragment() {


    private val mInterval = 1000L //millis
    private val networkCooldown = 5000L //millis
    private var mHandler: Handler? = null
    private var preferenceMap: Map<String, Any?>? = null
    private var onCooldown = SparseLongArray()
    private var deviceType = ITEM_VIEW_TYPE_SWITCH


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSubDevicesBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sub_devices, container, false)

        val subDevicesViewModel = ViewModelProviders.of(this).get(SubDevicesViewModel::class.java)

        val switchClickListener = ItemClickListener(subHomeClickListener = { switchItem ->
            onCooldown.append(switchItem.id, System.currentTimeMillis())
            preferenceMap?.let { map ->
                activity?.applicationContext?.let { context ->
                    StateRepository(
                        map,
                        context
                    ).sendSwitchUpdate(switchItem)
                }
            }
        })

        val adapter: SubHomeAdapter


        val arguments = SubDevicesFragmentArgs.fromBundle(arguments!!)
        deviceType = arguments.deviceType
        when (deviceType) {
            ITEM_VIEW_TYPE_SWITCH -> {
                adapter = SubHomeAdapter(switchClickListener)
                loadSwitches(subDevicesViewModel, adapter)
            }
            else -> {
                adapter = SubHomeAdapter(switchClickListener)
                loadSwitches(subDevicesViewModel, SubHomeAdapter(switchClickListener))
            }
        }


        val manager = LinearLayoutManager(activity)

        binding.viewModel = subDevicesViewModel
        binding.lifecycleOwner = this

        binding.deviceDevicesList.layoutManager = manager
        binding.deviceDevicesList.adapter = adapter


        preferenceMap =
            PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext).all
        mHandler = Handler()
        startRepeatingTask()

        return binding.root
    }

    private fun loadSwitches(subDevicesViewModel: SubDevicesViewModel, adapter: SubHomeAdapter) {
        subDevicesViewModel.switchItemList =
            Database.switches.value?.map { DHItem.SwitchItem(it) }!!

        Database.switches.observe(this, Observer { array ->
            for (switchComponent in array.iterator()) {
                val index =
                    subDevicesViewModel.switchItemList.indexOfFirst { item -> item.id == MAX_ITEMS_PER_TYPE * ITEM_VIEW_TYPE_SWITCH + switchComponent.id }
                if (index != -1 && subDevicesViewModel.switchItemList[index].state.value != switchComponent.isOn) {//check if needs update and index isn't non existing

                    if (onCooldown.getOrDefault(
                            subDevicesViewModel.switchItemList[index].id,
                            0
                        ) > System.currentTimeMillis() - networkCooldown
                    ) return@Observer //Still on cooldown

                    subDevicesViewModel.switchItemList[index].state.value = switchComponent.isOn
                    adapter.notifyItemChanged(index)
                }
            }
        })

        adapter.submitList(subDevicesViewModel.switchItemList)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRepeatingTask()
    }

    private val mStatusChecker: Runnable = object : Runnable {
        override fun run() {
            try {
                when (deviceType) {
                    ITEM_VIEW_TYPE_SWITCH -> {
                        activity?.applicationContext?.let { context ->
                            preferenceMap?.let { map ->
                                Database().refreshSwitchStates(
                                    map,
                                    context
                                )
                            }
                        }
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