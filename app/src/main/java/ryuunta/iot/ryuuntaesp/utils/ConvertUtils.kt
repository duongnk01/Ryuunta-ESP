package ryuunta.iot.ryuuntaesp.utils

fun convertKelvinToCelsius(kelvin: Double?): Int = if (kelvin == null) 0 else (kelvin - 273.15).toInt()