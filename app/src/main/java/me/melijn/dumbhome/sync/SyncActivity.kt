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

        val adapter = SyncDevicesAdapter(ItemClickListener { switchComponent ->
            println(switchComponent.toJSONString())
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
            adapter.submitList(it.map { switchComponent ->
                DHItem.SwitchItem(switchComponent)
            })
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
