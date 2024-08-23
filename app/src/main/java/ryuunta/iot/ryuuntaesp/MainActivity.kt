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

    private var currentHomePos: Int = 0

    private val userHomeAdapter: CustomSpinnerAdapter by lazy {
        CustomSpinnerAdapter(this, viewModel.listHomeUser)
    }

    override fun initViews() {

        binding.apply {
//            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_main_fragment) as NavHostFragment
//            setupActionBarWithNavController(navHostFragment.navController)
//            bottomNavigationView.setupWithNavController(navHostFragment.navController)
//            bottomNavigationView.menu.getItem(2).isEnabled = false  //disable placeholder item

//            spinHome.adapter = userHomeAdapter
//            spinHome.onItemSelectedListener = object : OnItemSelectedListener {
//                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                    if (position == userHomeAdapter.count - 1) {
//                        Toast.makeText(this@MainActivity, "add more", Toast.LENGTH_SHORT).show()
//
//                    } else {
////                        Toast.makeText(this@MainActivity, userHomeAdapter.getItem(position).text, Toast.LENGTH_SHORT).show()
//                        currentHomePos = position
//                        userHomeAdapter.currentPosSelected = currentHomePos
//                        userHomeAdapter.notifyDataSetChanged()
//                    }
//                    spinHome.setSelection(currentHomePos)
//                }
//
//                override fun onNothingSelected(p0: AdapterView<*>?) {
//
//                }
//
//            }
        }

    }

    override fun initEvents() {

//        binding.fabAddDevice.setOnClickListener {
//            startActivity(Intent(this, AddDeviceActivity::class.java))
//        }
    }

    fun headerHome(isShow: Boolean) {
//        if (isShow) {
//            binding.spinHome.show()
//        } else {
//            binding.spinHome.gone()
//        }

    }

    fun logout() {
        AuthenticationHelper.signOut {
            val intent = Intent(this, InitiationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

}