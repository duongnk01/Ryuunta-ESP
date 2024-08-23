package ryuunta.iot.ryuuntaesp

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ryuunta.iot.ryuuntaesp.base.BaseViewModel
import ryuunta.iot.ryuuntaesp.data.model.IconWithTextObj
import ryuunta.iot.ryuuntaesp.data.model.RoomObj
import ryuunta.iot.ryuuntaesp.data.model.WeatherDataCompilation
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

}