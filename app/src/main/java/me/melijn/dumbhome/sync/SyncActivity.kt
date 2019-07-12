package me.melijn.dumbhome.sync

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import me.melijn.dumbhome.R
import me.melijn.dumbhome.components.setSwitches
import me.melijn.dumbhome.database.Database
import me.melijn.dumbhome.databinding.ActivitySyncBinding
import me.melijn.dumbhome.io.SyncDeviceRepository
import me.melijn.dumbhome.objects.ItemClickListener

const val MAX_ITEMS_PER_TYPE = 1000
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

        val adapter = SyncDevicesAdapter(
            ItemClickListener(switchClickListener = { switchItem ->
                switchItem.currentState = !switchItem.currentState
                val invalid =
                    model.switchItems.firstOrNull { otherSwitchItem -> otherSwitchItem.id == switchItem.id }
                if (invalid != null) {
                    model.switchItems.remove(invalid)
                    invalid.currentState = switchItem.currentState
                    model.switchItems.add(invalid)
                } else model.switchItems.add(switchItem)
            }, submitClickListener = {
                val switchesForStorage = model.switchItems.filter { item -> item.currentState }
                    .map { item -> item.switchComponent }
                Database().setSwitches(this, switchesForStorage)

                //Exit
                onBackPressed()
            })
        )

        SyncDeviceRepository(
            PreferenceManager.getDefaultSharedPreferences(applicationContext).all,
            model
        )

        model.jsonDevices.observe(this, Observer {
            binding.textViewOne.text = ""
        })

        model.error.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })

        SyncDeviceRepository.switches.observe(this, Observer {

            val switchItemList = it.map { switchComponent ->
                val storedSwitchItem = model.switchItems.firstOrNull { it2 ->
                    switchComponent.id + ITEM_VIEW_TYPE_SWITCH * MAX_ITEMS_PER_TYPE == it2.id
                }

                val switchItem = DHItem.SwitchItem(switchComponent)
                switchItem.currentState = storedSwitchItem?.currentState
                        ?: (Database.switches.value?.count { dbSwitch -> dbSwitch.id == switchItem.switchComponent.id } ?: 0 > 0)
                switchItem
            }
            model.switchItems.clear()
            model.switchItems.addAll(switchItemList)
            adapter.submitList(switchItemList + listOf(DHItem.FooterItem(ITEM_VIEW_TYPE_FOOTER * MAX_ITEMS_PER_TYPE)))
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
