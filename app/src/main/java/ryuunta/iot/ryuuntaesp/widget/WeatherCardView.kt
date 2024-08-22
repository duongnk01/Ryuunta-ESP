package ryuunta.iot.ryuuntaesp.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.cardview.widget.CardView
import ryuunta.iot.ryuuntaesp.R
import java.io.IOException

class WeatherCardView: CardView {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context) : super(context, null)

    init {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.weather_card_view, this, true)

        val bgWeather: ImageView = findViewById(R.id.img_weather_bg)
        try {
            val inputStream = context.assets.open("nahida.png")
            val drawable = Drawable.createFromStream(inputStream, null)
            bgWeather.setImageDrawable(drawable)
            inputStream.close()
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        }
    }

    fun fetchDataWeather() {
        //TODO: fetch data from API
    }

}