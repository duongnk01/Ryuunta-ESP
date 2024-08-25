package ryuunta.iot.ryuuntaesp.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.data.model.WeatherDataCompilation
import ryuunta.iot.ryuuntaesp.data.network.UrlManage
import ryuunta.iot.ryuuntaesp.utils.RLog
import ryuunta.iot.ryuuntaesp.utils.convertKelvinToCelsius
import ryuunta.iot.ryuuntaesp.utils.gone
import ryuunta.iot.ryuuntaesp.utils.randomBackground
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick
import ryuunta.iot.ryuuntaesp.utils.show
import java.util.Locale

class WeatherCardView : FrameLayout {
    private val TAG = this.javaClass.simpleName

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context) : super(context, null)

    private var layoutWeather: RelativeLayout
    private var layoutError: LinearLayout
    private var animLoading: LottieAnimationView
    private var bgWeather: ImageView
    private var weatherState: ImageView
    private var temperature: TextView
    private var humidity: TextView
    private var fineDust: TextView
    private var weatherDesc: TextView

    private val airQualityPoint = mapOf(
        1 to context.getString(R.string.txt_good),
        2 to context.getString(R.string.txt_fair),
        3 to context.getString(R.string.txt_moderate),
        4 to context.getString(R.string.txt_poor),
        5 to context.getString(R.string.txt_very_poor),
    )

    private var loadingData: Boolean = false

    var isLoading: Boolean
        get() = loadingData
        set(value) {
            loadingData = value
            loadingData(value)
        }

    init {
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.weather_card_view, this, true)

        layoutWeather = findViewById(R.id.layout_weather)
        layoutError = findViewById(R.id.layout_error)
        bgWeather = findViewById(R.id.img_weather_bg)
        weatherState = findViewById(R.id.img_weather_state)
        temperature = findViewById(R.id.txt_temperature)
        humidity = findViewById(R.id.txt_humidity)
        fineDust = findViewById(R.id.txt_fine_dust)
        animLoading = findViewById(R.id.anim_load_data)
        weatherDesc = findViewById(R.id.txt_weather_desc)

    }

    fun fetchDataWeather(data: WeatherDataCompilation) {

        data.weatherData?.let { currWeather ->
            val iconUrl = String.format(
                Locale.getDefault(),
                UrlManage.weatherIconUrl,
                currWeather.weather[0].icon
            )
            RLog.d(TAG, iconUrl)
            Glide.with(context).load(iconUrl).placeholder(ColorDrawable(Color.GRAY))
                .fallback(R.drawable.ic_warning).into(weatherState)

            val temp = String.format(
                Locale.getDefault(),
                "%d°C",
                convertKelvinToCelsius(currWeather.main?.temp)
            )
            temperature.text = temp

            if (currWeather.weather.isNotEmpty()) {
                val descTemp = currWeather.weather[0].description
                weatherDesc.text = descTemp
            }

            val humid = "${currWeather.main?.humidity}%"
            humidity.text = humid

        }
        data.airPollution?.let { currAirPollution ->
            val aqi = currAirPollution.list[0].main?.aqi
            fineDust.text = airQualityAssessment(aqi ?: -1)
        }

    }

    private fun airQualityAssessment(aqi: Int): String {
        return airQualityPoint[aqi] ?: "null"
    }

    private fun loadingData(isLoading: Boolean) {
        RLog.d(TAG, "loading weather view")
        animLoading.show(!isLoading)
        if (!isLoading) {
            randomBackground(context, "chibi", bgWeather)
            layoutError.gone()
            layoutWeather.show()
        }
    }

    fun onLoadError(onReload: () -> Unit) {
        layoutError.show()
        layoutWeather.gone()

        layoutError.setPreventDoubleClick {
            onReload()
        }
    }

}