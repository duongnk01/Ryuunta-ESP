package ryuunta.iot.ryuuntaesp.main.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import ryuunta.iot.ryuuntaesp.MainViewModel
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.adapter.QuickScenarioListAdapter
import ryuunta.iot.ryuuntaesp.adapter.RoomSpinnerAdapter
import ryuunta.iot.ryuuntaesp.core.base.BaseFragment
import ryuunta.iot.ryuuntaesp.data.model.WeatherDataCompilation
import ryuunta.iot.ryuuntaesp.data.network.ResponseCode
import ryuunta.iot.ryuuntaesp.databinding.FragmentHomeBinding
import ryuunta.iot.ryuuntaesp.main.home.devices.DeviceListAdapter
import ryuunta.iot.ryuuntaesp.utils.developInProgress
import ryuunta.iot.ryuuntaesp.utils.gone
import ryuunta.iot.ryuuntaesp.utils.show
import ryuunta.iot.ryuuntaesp.utils.showDialogNotification

class HomeFragment : BaseFragment<FragmentHomeBinding, MainViewModel>(
    FragmentHomeBinding::inflate,
    MainViewModel::class.java
) {

    private val TAG = this.javaClass.simpleName

//    private var currentHomePos: Int = 0

    private var posRoomSelected: Int = 0
    private var currentRoomId = "0"

//    private val userHomeAdapter: HomeSpinnerAdapter by lazy {
//        HomeSpinnerAdapter(requireContext())
//    }

    private val roomSpinnerAdapter: RoomSpinnerAdapter by lazy {
        RoomSpinnerAdapter(requireContext())
    }

    private val quickScenarioListAdapter: QuickScenarioListAdapter by lazy {
        QuickScenarioListAdapter {
//            Toast.makeText(
//                requireContext(),
//                "Active scenario ${(it as ScenarioItem.QuickScenario).label}",
//                Toast.LENGTH_SHORT
//            ).show()
            requireContext().developInProgress(lifecycle)
        }
    }

    private val deviceListAdapter: DeviceListAdapter by lazy {
        DeviceListAdapter(onItemClick)
    }

    override fun initViews(savedInstanceState: Bundle?) {
        binding.apply {
            rcvQuickScenarios.adapter = quickScenarioListAdapter
            rcvQuickScenarios.overScrollMode = View.OVER_SCROLL_NEVER
            rcvDevices.adapter = deviceListAdapter
            rcvDevices.overScrollMode = View.OVER_SCROLL_NEVER




        }

//        viewModel.fetchHousesData {
//            RLog.d(TAG, "fetchHousesData: ${it.size}")
//            userHomeAdapter.listHomeUser = it
//            userHomeAdapter.notifyDataSetChanged()
//
//        }
//        binding.spinHome.adapter = userHomeAdapter
    }

    override fun onSetViewInfo() {
        super.onSetViewInfo()
        binding.apply {
            quickScenarioListAdapter.submitList(viewModel.quickScenarioList)
            viewModel.getHouseName {
                txtHeaderLabel.text = it
            }
            viewModel.fetchCurrWeather(requireContext())

            viewModel.mappingRoomSpin {
                roomSpinnerAdapter.listRoom = it
                roomSpinnerAdapter.notifyDataSetChanged()
            }
            spinRoom.adapter = roomSpinnerAdapter
            spinRoom.setSelection(0)

            viewModel.deviceLiveData.observe(viewLifecycleOwner) {
                viewModel.mappingDeviceUI { rItemList ->
                    if (rItemList.isEmpty()) {
                        binding.rcvDevices.gone()
                        binding.txtEmptyPlace.show()
                        binding.imgEmpty.show()
                    } else {
                        deviceListAdapter.submitList(rItemList)
                        binding.rcvDevices.show()
                        binding.txtEmptyPlace.gone()
                        binding.imgEmpty.gone()
                    }
                }
            }
        }

    }

    override fun initEvents() {
        binding.apply {
            appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                swiperRefresh.isEnabled = nestedScrollView.scrollY == 0 && verticalOffset == 0
            }

            swiperRefresh.setOnRefreshListener {
                swiperRefresh.setRefreshing(true)
//                isScreenLoading(true)
                Handler(Looper.getMainLooper()).postDelayed({
                    swiperRefresh.setRefreshing(false)
//                    isScreenLoading(false)
                    onRefreshHomePage()
                }, 500)

            }

            /*            binding.spinHome.onItemSelectedListener = object : OnItemSelectedListener {
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

                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {

                            }

                        }*/

            spinRoom.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    posRoomSelected = position
                    roomSpinnerAdapter.currRoomSelectedPosition = posRoomSelected
//                    customRoomSpinnerAdapter.notifyDataSetChanged()
//                    spinRoom.setSelection(posRoomSelected)
                    currentRoomId = roomSpinnerAdapter.listRoom[posRoomSelected].id

//                    viewModel.refreshDeviceList()
                    viewModel.refreshDeviceListByRoom(currentRoomId)

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        }
    }

    private fun onRefreshHomePage() {
        quickScenarioListAdapter.submitList(viewModel.quickScenarioList)
        viewModel.fetchCurrWeather(requireContext())
//        viewModel.refreshDeviceList()
        viewModel.refreshDeviceListByRoom(currentRoomId)
    }

    override fun handlerResponse(tag: String, data: Any?) {
        super.handlerResponse(tag, data)
        if (tag == ResponseCode.weatherData) {
            val weatherData = data as? WeatherDataCompilation
//            RLog.d(TAG, "weatherData: ${Gson().toJson(weatherData)}")
            binding.apply {
                weatherData?.let {
                    cvWeather.fetchDataWeather(it)
                }
            }
        }
    }

    override fun handlerError(tag: String, message: String) {
        super.handlerError(tag, message)
        if (tag == ResponseCode.weatherData) {
            binding.cvWeather.onLoadError {
                viewModel.fetchCurrWeather(requireContext())
            }
        }
        if (tag == ResponseCode.HOUSE_NOT_FOUND) {
            requireContext().showDialogNotification(
                R.string.oops,
                R.raw.anim_paimon_bikkurisuru,
                lifecycle,
                R.string.txt_house_not_found_cannot_get_room,
                errorMess = message,
                onConfirm = {

                }
            )
        }
    }

    override fun isScreenLoading(isLoading: Boolean) {
        binding.cvWeather.isLoading = isLoading
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}