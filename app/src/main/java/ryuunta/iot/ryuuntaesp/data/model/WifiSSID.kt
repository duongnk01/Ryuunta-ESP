package ryuunta.iot.ryuuntaesp.data.model

data class WifiSSID(
    val ssid: String,
    val bssid: String,
    val capabilities: String,
    val frequency: Int,
    val level: Int
)
