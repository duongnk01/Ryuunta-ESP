package ryuunta.iot.ryuuntaesp

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import ryuunta.iot.ryuuntaesp.helper.AuthenticationHelper
import ryuunta.iot.ryuuntaesp.core.base.BaseActivity
import ryuunta.iot.ryuuntaesp.databinding.ActivityMainBinding
import ryuunta.iot.ryuuntaesp.preference.SettingPreference
import ryuunta.iot.ryuuntaesp.utils.RLog

class RMainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(MainViewModel::class.java) {

    override fun getLayoutRes(): Int = R.layout.activity_main

    override fun initViews() {

    }

    override fun initEvents() {

    }

    fun requestBluetooth() {
        // check android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
//                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,

                    )
            )
        } else {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestEnableBluetooth.launch(enableBtIntent)
        }
    }

    private val requestEnableBluetooth =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // granted
            } else {
                // denied
            }
        }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                RLog.d("MyTag", "${it.key} = ${it.value}")
            }
        }

    fun logout() {
        AuthenticationHelper.signOut {
            SettingPreference.clearAllData(this)

            val intent = Intent(this, InitiationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

}