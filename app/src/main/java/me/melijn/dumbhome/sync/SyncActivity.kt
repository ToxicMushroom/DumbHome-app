package me.melijn.dumbhome.sync

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import me.melijn.dumbhome.ItemClickListener
import me.melijn.dumbhome.R
import me.melijn.dumbhome.databinding.ActivitySyncBinding
import me.melijn.dumbhome.io.DeviceRepository

class SyncActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySyncBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_sync
        )

        supportActionBar?.setTitle(R.string.title_activity_sync)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val model: SyncViewModel = ViewModelProviders.of(this).get(SyncViewModel::class.java)

        binding.lifecycleOwner = this
        binding.syncViewModel = model

        val adapter = SyncDevicesAdapter(ItemClickListener { switchItem ->
            switchItem.currentState = !switchItem.currentState
            println(switchItem.switchComponent.toJSONString())
            println(switchItem.currentState)

            val invalid =
                model.switchItems.firstOrNull { otherSwitchItem -> otherSwitchItem.id == switchItem.id }
            if (invalid != null) {
                model.switchItems.remove(invalid)
                invalid.currentState = switchItem.currentState
                model.switchItems.add(invalid)
            } else model.switchItems.add(switchItem)
        })

        DeviceRepository(
            PreferenceManager.getDefaultSharedPreferences(applicationContext).all,
            model
        )

        model.jsonDevices.observe(this, Observer {
            binding.textViewOne.text = ""
        })

        model.error.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })

        model.clicked.observe(this, Observer {

        })

        DeviceRepository.switches.observe(this, Observer {
            val switchItemList = it.map { switchComponent ->
                val storedSwitchITem = model.switchItems.firstOrNull { it2 ->
                    switchComponent.id * 10 + ITEM_VIEW_TYPE_SWITCH == it2.id
                }

                val switchItem = DHItem.SwitchItem(switchComponent)
                switchItem.currentState = storedSwitchITem?.currentState ?: false
                switchItem
            }
            model.switchItems.clear()
            model.switchItems.addAll(switchItemList)
            adapter.submitList(switchItemList)
        })

        val manager = LinearLayoutManager(this)
        binding.deviceSyncList.layoutManager = manager
        binding.deviceSyncList.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}
