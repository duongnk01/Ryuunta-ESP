package ryuunta.iot.ryuuntaesp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ryuunta.iot.ryuuntaesp.core.base.BaseViewModel
import ryuunta.iot.ryuuntaesp.data.model.DeviceItem
import ryuunta.iot.ryuuntaesp.data.model.DeviceObj
import ryuunta.iot.ryuuntaesp.data.model.HouseObj
import ryuunta.iot.ryuuntaesp.data.model.IconWithTextObj
import ryuunta.iot.ryuuntaesp.data.model.RItem
import ryuunta.iot.ryuuntaesp.data.model.ScenarioItem
import ryuunta.iot.ryuuntaesp.data.model.WeatherDataCompilation
import ryuunta.iot.ryuuntaesp.data.network.ResponseCode
import ryuunta.iot.ryuuntaesp.helper.DeviceHelper
import ryuunta.iot.ryuuntaesp.helper.GroupHelper
import ryuunta.iot.ryuuntaesp.main.home.devices.DeviceViewType
import ryuunta.iot.ryuuntaesp.main.home.devices.listDeviceType

class MainViewModel : BaseViewModel() {

    private val _deviceLiveData = MutableLiveData<List<DeviceObj>>(listOf())
    val deviceLiveData: LiveData<List<DeviceObj>> = _deviceLiveData

    var currentPager = 0

    private var deviceHelper: DeviceHelper = DeviceHelper()
    private var groupHelper: GroupHelper = GroupHelper()

    val listHomeUser = listOf(
        IconWithTextObj(0, R.drawable.ic_no_face, "Ryuunta"),
        IconWithTextObj(1, R.drawable.klee, "Ryuunta 2"),
        IconWithTextObj(2, R.drawable.ic_no_face, "Ryuunta 3")
    )

    val quickScenarioList = listOf(
        ScenarioItem.QuickScenario("Tắt đèn hành lang"),
        ScenarioItem.QuickScenario("Đi ngủ"),
        ScenarioItem.QuickScenario("đi ra ngoài"),
        ScenarioItem.QuickScenario("bật tất cả quạt"),
        ScenarioItem.QuickScenario("Đi làm"),
    )

    fun getHouseName(onCompleted: (String) -> Unit) {
        groupHelper.getHouseName(onCompleted)
    }

    fun getRoomName(roomId: String, onCompleted: (String) -> Unit) {
        groupHelper.getRoomName(roomId) {
            onCompleted(it)
        }

    }

    fun getDeviceName(deviceId: String, onCompleted: (String) -> Unit) {
        deviceHelper.getDeviceName(deviceId, onCompleted)
    }

    fun fetchHousesData(onCompleted: (List<HouseObj>) -> Unit) {
        groupHelper.getHouseList {
            onCompleted(it)
        }
    }

    fun mappingRoomSpin(onRoomUIReady: (List<HouseObj.RoomObj>) -> Unit) {

        groupHelper.getRoomsInHouse {
            val rooms = mutableListOf<HouseObj.RoomObj>()
            rooms.addAll(it)
            onRoomUIReady(rooms)
        }
    }


    fun fetchCurrWeather(context: Context) {
        val lat = 21.0294498
        val lon = 105.8544441
        val apiKey = context.getString(R.string.open_weather_map_api_key)
        loading.postValue(true)

        job.launch {
            val fetchWeather = retrofitService.getCurrentWeather(lat, lon, apiKey)
            val fetchAirPollution = retrofitService.getCurrAirPollution(lat, lon, apiKey)
            val data = listOf(
                async { fetchWeather },
                async { fetchAirPollution }
            )

            withContext(Dispatchers.Main) {
                loading.postValue(false)
                val result = data.awaitAll()

                if (result.all { it.isSuccessful }) {
                    val weatherData = WeatherDataCompilation(
                        result[0].body() as? WeatherDataCompilation.WeatherData,
                        result[1].body() as? WeatherDataCompilation.AirPollution
                    )
                    _baseResult.postValue(mapOf(ResponseCode.weatherData to weatherData))
                } else {
                    result.forEach {
                        if (!it.isSuccessful) {
                            onError(mapOf(ResponseCode.weatherData to it.message()))
                        }
                    }
                }
            }
        }

    }

    //roomId = null is mean get all device of house
    fun refreshDeviceListByRoom(roomId: String = "0") {
        deviceHelper.getDevicesByRoom(roomId) {
            _deviceLiveData.value = it
        }
    }

    fun mappingDeviceUI(onDeviceUIReady: (List<RItem>) -> Unit) {
        viewModelScope.launch {
            mappingDeviceList(onDeviceUIReady)
        }
    }

    private suspend fun mappingDeviceList(onHomeUIReady: (List<RItem>) -> Unit) {

        val deviceFlow = _deviceLiveData.value?.asFlow() ?: flow { }

        val rItemList = mutableListOf<RItem>()

        listDeviceType.forEach { type ->
            val deviceViewType = deviceFlow.filter {
                it.type == type.name
            }.toList()
            if (deviceViewType.isNotEmpty()) {
                when (type) {
                    DeviceViewType.SWITCH_BUTTON, DeviceViewType.SWITCH_4_BUTTON -> {
                        deviceViewType.forEach { item ->
                            rItemList.add(
                                DeviceItem.SwitchButton(
                                    type.code,
                                    R.drawable.ic_switch_button,
                                    R.string.txt_switch_button,
                                    item
                                )
                            )
                        }

                    }

                    DeviceViewType.FAN_REMOTE -> {
                        deviceViewType.forEach { item ->
                            rItemList.add(
                                DeviceItem.FanRemote(
                                    type.code,
                                    R.drawable.ic_fan_remote,
                                    R.string.txt_fan_remote,
                                    item
                                )
                            )
                        }

                    }

                    else -> {}
                }
            }


        }

        withContext(Dispatchers.Main) {
            onHomeUIReady(rItemList)
        }

    }

    override fun onCleared() {
        super.onCleared()

    }

}