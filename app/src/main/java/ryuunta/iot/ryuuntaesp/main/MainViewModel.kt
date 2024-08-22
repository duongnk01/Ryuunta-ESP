package ryuunta.iot.ryuuntaesp.main

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.base.BaseViewModel
import ryuunta.iot.ryuuntaesp.data.model.IconWithTextObj
import ryuunta.iot.ryuuntaesp.data.model.RoomObj
import ryuunta.iot.ryuuntaesp.data.network.ResponseCode

class MainViewModel() : BaseViewModel() {

    val listHomeUser = listOf(
        IconWithTextObj(0, R.drawable.ic_no_face, "Ryuunta"),
        IconWithTextObj(1, R.drawable.klee, "Ryuunta 2"),
        IconWithTextObj(2, R.drawable.ic_no_face, "Ryuunta 3")
    )

    val roomList = listOf(
        RoomObj(0, "Tất cả"),
        RoomObj(1, "Phòng khách"),
        RoomObj(2, "Nhà bếp"),
        RoomObj(3, "Gara"),
        RoomObj(4, "Phòng ngủ")
    )

    fun fetchCurrWeather(context: Context) {
        val lat = 21.0294498
        val lon = 105.8544441
        val apiKey = context.getString(R.string.open_weather_map_api_key)
        job.launch {
            val response = retrofitService.getCurrentWeather(lat, lon, apiKey)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val data = mapOf(ResponseCode.weatherData to response.body())
                    baseResult.postValue(data)
                } else {
                    onError(mapOf(ResponseCode.weatherData to response.message()))
                }
            }
        }

    }

}