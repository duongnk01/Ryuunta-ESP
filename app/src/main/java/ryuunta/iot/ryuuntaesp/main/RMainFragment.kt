package ryuunta.iot.ryuuntaesp.main

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ryuunta.iot.ryuuntaesp.MainViewModel
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.adapter.HomeSpinnerAdapter
import ryuunta.iot.ryuuntaesp.core.base.BaseFragment
import ryuunta.iot.ryuuntaesp.core.base.Config
import ryuunta.iot.ryuuntaesp.databinding.FragmentRyuuntaMainBinding
import ryuunta.iot.ryuuntaesp.preference.AppPref
import ryuunta.iot.ryuuntaesp.preference.SettingPreference
import ryuunta.iot.ryuuntaesp.utils.RLog
import ryuunta.iot.ryuuntaesp.utils.gone
import ryuunta.iot.ryuuntaesp.utils.show

class RMainFragment : BaseFragment<FragmentRyuuntaMainBinding, MainViewModel>(
    FragmentRyuuntaMainBinding::inflate,
    MainViewModel::class.java
) {

    private var TAG = "RMainFragment"

    private var currentHomePos: Int = 0

    private val userHomeAdapter: HomeSpinnerAdapter by lazy {
        HomeSpinnerAdapter(requireContext())
    }

    override fun initViews(savedInstanceState: Bundle?) {
        binding.apply {
            initViewPager()
            if (viewModel.currentPager == 2 || viewModel.currentPager == 3) {
                spinHome.gone()
            }
            vpMain.currentItem = viewModel.currentPager

            bottomNavigationView.menu.getItem(2).isEnabled = false  //disable placeholder item
            bottomNavigationView.setOnItemSelectedListener {
                vpMain.currentItem = when (it.itemId) {
                    R.id.homeFragment -> {
                        spinHome.show()
                        0
                    }

                    R.id.notificationFragment -> {
                        spinHome.show()
                        1
                    }

                    R.id.manageFragment -> {
                        spinHome.gone()
                        2
                    }

                    R.id.userFragment -> {
                        spinHome.gone()
                        3
                    }

                    else -> {
                        spinHome.show()
                        0
                    }
                }
                return@setOnItemSelectedListener true
            }

//            SettingPreference.getData(
//                requireContext(),
//                listOf(AppPref.CURRENT_HOUSE_ID)
//            ) { pref ->
//                if (pref.data.isNotEmpty()) {
//                    currentHomePos =
//                        userHomeAdapter.listHomeUser.indexOfFirst { house -> house.id == pref.data }
//                    spinHome.setSelection(currentHomePos)
//                }
//            }
            spinHome.adapter = userHomeAdapter

            viewModel.fetchHousesData {
                RLog.d(TAG, "fetchHousesData: ${it.size}")
                userHomeAdapter.listHomeUser = it
                userHomeAdapter.notifyDataSetChanged()

            }

        }

    }

    override fun initEvents() {
        super.initEvents()
        binding.fabAddDevice.setOnClickListener {
            navController.navigate(R.id.action_ryuuntaMainFragment_to_addDeviceFragment)
            viewModel.currentPager = binding.vpMain.currentItem
        }

        binding.spinHome.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                currentHomePos = position
                userHomeAdapter.currentPosSelected = currentHomePos
//                userHomeAdapter.notifyDataSetChanged()
//                binding.spinHome.setSelection(currentHomePos)

                val currHouseId = userHomeAdapter.getItem(currentHomePos).id
                CoroutineScope(Dispatchers.Default).launch {
                    SettingPreference.saveData(
                        requireContext(),
                        mapOf(AppPref.CURRENT_HOUSE_ID to currHouseId)
                    )
                    withContext(Dispatchers.Main) {
                        Config.currentHouseId = currHouseId
                    }
                }

                viewModel.mappingRoomSpin {  }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }


    }
}