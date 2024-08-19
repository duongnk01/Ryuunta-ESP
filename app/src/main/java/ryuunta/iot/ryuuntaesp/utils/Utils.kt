package ryuunta.iot.ryuuntaesp.utils

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import com.google.gson.Gson
import ryuunta.iot.ryuuntaesp.data.model.WifiSSID

fun scanWifi(context: Context): List<WifiSSID> {
    val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val wifiList = arrayListOf<WifiSSID>()
    wifiManager.dhcpInfo.ipAddress
    // Bật Wi-Fi nếu chưa bật
    if (!wifiManager.isWifiEnabled) {
        wifiManager.isWifiEnabled = true
    }

    // Khởi động quá trình quét
    wifiManager.startScan()

    // Lấy danh sách các mạng Wi-Fi được phát hiện
    val scanResults = wifiManager.scanResults

    // Duyệt qua danh sách mạng
    for (scanResult in scanResults) {
        Log.d("scanWifi", "Wifi scan:  ${Gson().toJson(scanResult)}")
        // Thêm mạng vào danh sách
        wifiList.add(
            WifiSSID(
                scanResult.SSID,
                scanResult.BSSID,
                scanResult.capabilities,
                scanResult.frequency,
                scanResult.level,
            )
        )
    }

    // Trả về danh sách mạng
    return wifiList.sortedBy { it.level }.asReversed()
}

fun convertDpToPixel(dp: Float, context: Context): Float {
    val resources = context.resources
    val metrics = resources.displayMetrics

    val px = dp * (metrics.densityDpi / 160f)
    return px
}

fun convertPixelsToDp(px: Float, context: Context): Float {
    val resources = context.resources
    val metrics = resources.displayMetrics
    val dp = px / (metrics.densityDpi / 160f)
    return dp
}