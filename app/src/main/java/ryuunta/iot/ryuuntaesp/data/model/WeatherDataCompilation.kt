package ryuunta.iot.ryuuntaesp.data.model

import com.google.gson.annotations.SerializedName

data class WeatherDataCompilation(
    val weatherData: WeatherData? = null,
    val airPollution: AirPollution? = null
) {
    data class WeatherData(
//        @SerializedName("coord"      ) var coord      : Coord?             = Coord(),
        @SerializedName("weather"    ) var weather    : ArrayList<Weather> = arrayListOf(),
//        @SerializedName("base"       ) var base       : String?            = null,
        @SerializedName("main"       ) var main       : Main?              = Main(),
//        @SerializedName("visibility" ) var visibility : Int?               = null,
//        @SerializedName("wind"       ) var wind       : Wind?              = Wind(),
//    @SerializedName("rain"       ) var rain       : Rain?              = Rain(),
//        @SerializedName("clouds"     ) var clouds     : Clouds?            = Clouds(),
//        @SerializedName("dt"         ) var dt         : Int?               = null,
//        @SerializedName("sys"        ) var sys        : Sys?               = Sys(),
//        @SerializedName("timezone"   ) var timezone   : Int?               = null,
//        @SerializedName("id"         ) var id         : Int?               = null,
//        @SerializedName("name"       ) var name       : String?            = null,
//        @SerializedName("cod"        ) var cod        : Int?               = null
    ) {
        data class Coord (
            @SerializedName("lon" ) var lon : Double? = null,
            @SerializedName("lat" ) var lat : Double? = null
        )

        data class Weather (
            @SerializedName("id"          ) var id          : Int?    = null,
            @SerializedName("main"        ) var main        : String? = null,
            @SerializedName("description" ) var description : String? = null,
            @SerializedName("icon"        ) var icon        : String? = null
        )

        data class Main (
            @SerializedName("temp"       ) var temp      : Double? = null,
//            @SerializedName("feels_like" ) var feelsLike : Double? = null,
//            @SerializedName("temp_min"   ) var tempMin   : Double? = null,
//            @SerializedName("temp_max"   ) var tempMax   : Double? = null,
            @SerializedName("pressure"   ) var pressure  : Int?    = null,
            @SerializedName("humidity"   ) var humidity  : Int?    = null,
//            @SerializedName("sea_level"  ) var seaLevel  : Int?    = null,
//            @SerializedName("grnd_level" ) var grndLevel : Int?    = null
        )

        data class Wind (
            @SerializedName("speed" ) var speed : Double? = null,
            @SerializedName("deg"   ) var deg   : Int?    = null,
            @SerializedName("gust"  ) var gust  : Double? = null
        )

//    data class Rain (
//        @SerializedName("1h" ) var 1h : Double? = null
//    )

        data class Clouds (
            @SerializedName("all" ) var all : Int? = null
        )

        data class Sys (
            @SerializedName("type"    ) var type    : Int?    = null,
            @SerializedName("id"      ) var id      : Int?    = null,
            @SerializedName("country" ) var country : String? = null,
            @SerializedName("sunrise" ) var sunrise : Int?    = null,
            @SerializedName("sunset"  ) var sunset  : Int?    = null
        )

    }

    data class AirPollution(
//        @SerializedName("coord" ) var coord : Coord?          = Coord(),
        @SerializedName("list"  ) var list  : ArrayList<ListPollution> = arrayListOf()

    ) {
        data class Coord (
            @SerializedName("lat" ) var lat : Double? = null,
            @SerializedName("lon" ) var lon : Double? = null
        )
        data class ListPollution (
            @SerializedName("main"       ) var main       : Main?       = Main(),
//            @SerializedName("components" ) var components : Components? = Components(),
//            @SerializedName("dt"         ) var dt         : Int?        = null
        )
        data class Main (

            @SerializedName("aqi" ) var aqi : Int? = null   //Đánh giá chất lượng kk từ 1(tốt) -> 5(rất kém)

        )
        data class Components (

            @SerializedName("co"    ) var co   : Double? = null,
            @SerializedName("no"    ) var no   : Double? = null,
            @SerializedName("no2"   ) var no2  : Double? = null,
            @SerializedName("o3"    ) var o3   : Double? = null,
            @SerializedName("so2"   ) var so2  : Double? = null,
            @SerializedName("pm2_5" ) var pm25 : Double? = null,
            @SerializedName("pm10"  ) var pm10 : Double? = null,
            @SerializedName("nh3"   ) var nh3  : Double? = null

        )
    }
}


