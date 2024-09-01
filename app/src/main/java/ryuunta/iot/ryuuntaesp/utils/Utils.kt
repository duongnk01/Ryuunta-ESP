package ryuunta.iot.ryuuntaesp.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.MacAddress
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.os.PatternMatcher
import android.widget.ImageView
import com.google.gson.Gson
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.data.model.WifiSSID
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.Random
import java.util.UUID

fun randomId(): String {
    Thread.sleep(1)     //delay to avoid duplicate timestamp
    //add timestamp in id for sorted
    return "${System.currentTimeMillis()}+${UUID.randomUUID()}"       //uuid form: timestamp.uuid
}


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
        RLog.d("scanWifi", "Wifi scan:  ${Gson().toJson(scanResult)}")
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
    return wifiList.filter { it.frequency < 2500 }.sortedBy { it.level }.asReversed()
}

fun scanESP8266Wifi(context: Context): List<WifiSSID> {
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
        RLog.d("scanWifi", "Wifi esp8266scan:  ${Gson().toJson(scanResult)}")
        if (scanResult.SSID.contains("RYUUNTA")) {
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

    }

    // Trả về danh sách mạng
    return wifiList.filter { it.frequency < 2500 }.sortedBy { it.level }.asReversed()
}

fun Context.connectToWifi(ssid: String, bssid: String) {

    val wifiNetworkSpecifier = WifiNetworkSpecifier.Builder()
        .setSsidPattern(PatternMatcher(ssid, PatternMatcher.PATTERN_PREFIX))
        .setBssidPattern(MacAddress.fromString(bssid), MacAddress.fromString("ff:ff:ff:00:00:00"))
        .build()

    val request = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .setNetworkSpecifier(wifiNetworkSpecifier)
        .build()

    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val nwtCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            RLog.d("duongnk", "onAvailable")

        }

        override fun onUnavailable() {
            RLog.d("duongnk", "onUnavailable")

        }

    }

    connectivityManager.requestNetwork(request, nwtCallback)
    connectivityManager.unregisterNetworkCallback(nwtCallback)

}

fun sendDataToESP8266(context: Context, data: String, ipAddress: String, port: Int) {
//    val queue = Volley.newRequestQueue(context) // Thay thế 'context' bằng context của Activity/Fragment

    try {
        RLog.e("Sending", "data = $data")
        RLog.e("Sending", "sending data...")

        val socket = DatagramSocket()
        val message = data.toByteArray()
        val packet = DatagramPacket(message, message.size, InetAddress.getByName(ipAddress), port)
        socket.send(packet)
        socket.close()
        RLog.e("Sending", "sending data done")
    } catch (e: Exception) {
        // Xử lý lỗi
        e.printStackTrace()
        RLog.e("Error", "Error sending data: ${e.message}")
    }
}

fun Context.convertDpToPixel(dp: Float): Float {
    val resources = resources
    val metrics = resources.displayMetrics

    val px = dp * (metrics.densityDpi / 160f)
    return px
}

fun Context.convertPixelsToDp(px: Float): Float {
    val resources = resources
    val metrics = resources.displayMetrics
    val dp = px / (metrics.densityDpi / 160f)
    return dp
}

fun <K, V> splitHashMap(map: Map<K, V>, chunkSize: Int): List<Map<K, V>> {
    val list = map.toList()
    val subLists = list.chunked(chunkSize)
    return subLists.map { it.toMap() }
}

fun getRandomSticker(context: Context, assetPath: String, iv: ImageView) {
    RLog.d("getRandomSticker", "load random sticker")
    val assetManager = context.assets
    try {
        val files = assetManager.list(assetPath)
        if (!files.isNullOrEmpty()) {
            val randomAssetName = Random().nextInt(files.size)
            val randomFile = files[randomAssetName]

            val inputStream = context.assets.open("$assetPath/$randomFile")
            val drawable = Drawable.createFromStream(inputStream, null)
            iv.setImageDrawable(drawable)
            inputStream.close()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun getAssetBackground(context: Context, assetName: String, iv: ImageView) {
    RLog.d("getAssetBackground", "load specific background")
    try {
        val inputStream = context.assets.open("background/$assetName")
        val drawable = Drawable.createFromStream(inputStream, null)
        iv.setImageDrawable(drawable)
        inputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()

    }
}

fun getRandomColor(): Int {
    val listColor = listOf(
        R.color.pastel_blue,
        R.color.pastel_pink,
        R.color.pastel_blue_sea,
        R.color.pastel_lavender_blue,
//        R.color.pastel_pink_light,
        R.color.pastel_green_mint,
//        R.color.pastel_blue_sea_light,
//        R.color.pastel_blue_sky_light,
        R.color.pastel_lavender_purple,
//        R.color.pastel_green_mint_light,
        R.color.pastel_orange_1,
        R.color.pastel_orange_2,
        R.color.pastel_peach_1,
        R.color.pastel_peach_2,
        R.color.pastel_green_1,
        R.color.pastel_green_2,
    )
    return listColor[Random().nextInt(listColor.size)]

}