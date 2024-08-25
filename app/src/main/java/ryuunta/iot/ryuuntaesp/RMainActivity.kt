package ryuunta.iot.ryuuntaesp

import android.content.Intent
import ryuunta.iot.ryuuntaesp.helper.AuthenticationHelper
import ryuunta.iot.ryuuntaesp.core.base.BaseActivity
import ryuunta.iot.ryuuntaesp.databinding.ActivityMainBinding
import ryuunta.iot.ryuuntaesp.preference.SettingPreference

class RMainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(MainViewModel::class.java) {

    override fun getLayoutRes(): Int = R.layout.activity_main

    override fun initViews() {

    }

    override fun initEvents() {

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