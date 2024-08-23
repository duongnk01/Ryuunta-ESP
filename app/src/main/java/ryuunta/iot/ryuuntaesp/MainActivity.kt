package ryuunta.iot.ryuuntaesp

import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ryuunta.iot.ryuuntaesp.adapter.CustomSpinnerAdapter
import ryuunta.iot.ryuuntaesp.authentication.AuthenticationHelper
import ryuunta.iot.ryuuntaesp.base.BaseActivity
import ryuunta.iot.ryuuntaesp.databinding.ActivityMainBinding
import ryuunta.iot.ryuuntaesp.devices.AddDeviceActivity
import ryuunta.iot.ryuuntaesp.utils.gone
import ryuunta.iot.ryuuntaesp.utils.show

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(MainViewModel::class.java) {

    override fun getLayoutRes(): Int = R.layout.activity_main

    override fun initViews() {

    }

    override fun initEvents() {

    }

    fun logout() {
        AuthenticationHelper.signOut {
            val intent = Intent(this, InitiationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

}