package ryuunta.iot.ryuuntaesp

import androidx.navigation.fragment.NavHostFragment
import ryuunta.iot.ryuuntaesp.base.BaseActivity
import ryuunta.iot.ryuuntaesp.databinding.ActivityInitiationBinding

class InitiationActivity : BaseActivity<ActivityInitiationBinding, InitiationViewModel>(InitiationViewModel::class.java) {
    override fun getLayoutRes(): Int {
        return R.layout.activity_initiation
    }

    override fun initViews() {
        binding.apply {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_initiation_fragment) as NavHostFragment
            navController = navHostFragment.navController
        }
    }

    override fun initEvents() {
        //TODO("Not yet implemented")
    }
}