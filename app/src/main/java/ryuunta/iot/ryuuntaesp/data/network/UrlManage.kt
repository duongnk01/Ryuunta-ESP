package ryuunta.iot.ryuuntaesp.data.network

object UrlManage {
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    const val getCurrentWeather = "${BASE_URL}weather?"
    const val getCurrAirPollution = "${BASE_URL}air_pollution?"

    const val weatherIconUrl = "https://openweathermap.org/img/wn/%s@2x.png"

}