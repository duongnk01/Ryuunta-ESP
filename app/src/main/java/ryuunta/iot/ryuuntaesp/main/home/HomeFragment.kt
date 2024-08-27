package ryuunta.iot.ryuuntaesp.main.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import ryuunta.iot.ryuuntaesp.MainViewModel
import ryuunta.iot.ryuuntaesp.adapter.QuickScenarioListAdapter
import ryuunta.iot.ryuuntaesp.adapter.RoomSpinnerAdapter
import ryuunta.iot.ryuuntaesp.core.base.BaseFragment
import ryuunta.iot.ryuuntaesp.data.model.ScenarioItem
import ryuunta.iot.ryuuntaesp.data.model.WeatherDataCompilation
import ryuunta.iot.ryuuntaesp.data.network.ResponseCode
import ryuunta.iot.ryuuntaesp.databinding.FragmentHomeBinding
import ryuunta.iot.ryuuntaesp.main.home.devices.DeviceListAdapter
import ryuunta.iot.ryuuntaesp.utils.developInProgress
import ryuunta.iot.ryuuntaesp.utils.gone
import ryuunta.iot.ryuuntaesp.utils.show

class HomeFragment : BaseFragment<FragmentHomeBinding, MainViewModel>(
    FragmentHomeBinding::inflate,
    MainViewModel::class.java
) {

    private val TAG = this.javaClass.simpleName

    private var posRoomSelected: Int = 0

    private val customRoomSpinnerAdapter: RoomSpinnerAdapter by lazy {
        RoomSpinnerAdapter(requireContext(), viewModel.roomList)
    }

    private val quickScenarioListAdapter: QuickScenarioListAdapter by lazy {
        QuickScenarioListAdapter {
            Toast.makeText(
                requireContext(),
                "Active scenario ${(it as ScenarioItem.QuickScenario).label}",
                Toast.LENGTH_SHORT
            ).show()
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
            quickScenarioListAdapter.submitList(viewModel.quickScenarioList)
            rcvDevices.adapter = deviceListAdapter
            rcvDevices.overScrollMode = View.OVER_SCROLL_NEVER
        }
        viewModel.fetchCurrWeather(requireContext())

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

            spinRoom.adapter = customRoomSpinnerAdapter
            spinRoom.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
//                    if (position == customRoomSpinnerAdapter.count - 1) {
//                        Toast.makeText(requireContext(), "thêm phòng mới", Toast.LENGTH_SHORT)
//                            .show()
//                    } else {
//
//                    }
                    posRoomSelected = position
                    customRoomSpinnerAdapter.currRoomSelectedPosition = posRoomSelected
                    customRoomSpinnerAdapter.notifyDataSetChanged()
                    spinRoom.setSelection(posRoomSelected)

                    viewModel.refreshDeviceList()

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        }
    }

    private fun onRefreshHomePage() {
        quickScenarioListAdapter.submitList(viewModel.quickScenarioList)
        viewModel.fetchCurrWeather(requireContext())
        viewModel.refreshDeviceList()
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
    }

    override fun isScreenLoading(isLoading: Boolean) {
        binding.cvWeather.isLoading = isLoading
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}