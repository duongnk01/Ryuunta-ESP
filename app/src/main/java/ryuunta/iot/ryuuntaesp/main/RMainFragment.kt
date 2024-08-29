package ryuunta.iot.ryuuntaesp.main

import android.os.Bundle
import ryuunta.iot.ryuuntaesp.MainViewModel
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.core.base.BaseFragment
import ryuunta.iot.ryuuntaesp.databinding.FragmentRyuuntaMainBinding

class RMainFragment : BaseFragment<FragmentRyuuntaMainBinding, MainViewModel>(
    FragmentRyuuntaMainBinding::inflate,
    MainViewModel::class.java
) {
    private var TAG = "RMainFragment"

    override fun initViews(savedInstanceState: Bundle?) {
        binding.apply {
            initViewPager()

            vpMain.currentItem = viewModel.currentPager

            bottomNavigationView.menu.getItem(2).isEnabled = false  //disable placeholder item
            bottomNavigationView.setOnItemSelectedListener {
                vpMain.currentItem = when (it.itemId) {
                    R.id.homeFragment -> 0

                    R.id.notificationFragment -> 1

                    R.id.manageFragment -> 2

                    R.id.userFragment -> 3

                    else -> 0
                }
                return@setOnItemSelectedListener true
            }
        }
    }

    override fun initEvents() {
        super.initEvents()
        binding.fabAddDevice.setOnClickListener {
            navController.navigate(R.id.action_ryuuntaMainFragment_to_addDeviceFragment)
            viewModel.currentPager = binding.vpMain.currentItem
        }
    }
}