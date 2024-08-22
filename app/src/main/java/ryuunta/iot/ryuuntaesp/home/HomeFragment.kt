package ryuunta.iot.ryuuntaesp.home

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import com.google.gson.Gson
import ryuunta.iot.ryuuntaesp.adapter.RoomSpinnerAdapter
import ryuunta.iot.ryuuntaesp.base.BaseFragment
import ryuunta.iot.ryuuntaesp.data.model.WeatherDataObj
import ryuunta.iot.ryuuntaesp.data.network.ResponseCode
import ryuunta.iot.ryuuntaesp.databinding.FragmentHomeBinding
import ryuunta.iot.ryuuntaesp.main.MainActivity
import ryuunta.iot.ryuuntaesp.main.MainViewModel
import ryuunta.iot.ryuuntaesp.utils.RLog

class HomeFragment : BaseFragment<FragmentHomeBinding, MainViewModel>(
    FragmentHomeBinding::inflate,
    MainViewModel::class.java
) {

    private val TAG = this.javaClass.simpleName

    private var posRoomSelected: Int = 0

    private val customRoomSpinnerAdapter: RoomSpinnerAdapter by lazy {
        RoomSpinnerAdapter(requireContext(), viewModel.roomList)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).headerHome(true)
    }

    override fun initViews(view: View) {
        binding.apply {

        }

        viewModel.fetchCurrWeather(requireContext())
    }

    override fun initEvents() {
        binding.apply {
            appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                swiperRefresh.isEnabled = nestedScrollView.scrollY == 0 && verticalOffset == 0
            }

            swiperRefresh.setOnRefreshListener {
                swiperRefresh.setRefreshing(true)
                showLoading()
                Handler(Looper.getMainLooper()).postDelayed({
                    swiperRefresh.setRefreshing(false)
                    hideLoading()
                }, 3000)

            }

            spinRoom.adapter = customRoomSpinnerAdapter
            spinRoom.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (position == customRoomSpinnerAdapter.count - 1) {
                        Toast.makeText(requireContext(), "thêm phòng mới", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        posRoomSelected = position
                        customRoomSpinnerAdapter.currRoomSelectedPosition = posRoomSelected
                        customRoomSpinnerAdapter.notifyDataSetChanged()
                    }
                    spinRoom.setSelection(posRoomSelected)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        }
    }

    override fun handlerResponse(tag: String, data: Any?) {
        super.handlerResponse(tag, data)
        if (tag == ResponseCode.weatherData) {
            val weatherData = data as? WeatherDataObj
            RLog.d(TAG, "weatherData: ${Gson().toJson(weatherData)}")
        }
    }
}