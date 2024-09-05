package ryuunta.iot.ryuuntaesp.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.widget.ImageView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        if (!scanResult.SSID.contains("RYUUNTA_ESP")) {
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

fun getCurrentWifiConnection(context: Context): String {
    val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val wifiInfo = wifiManager.connectionInfo


    if (wifiInfo.networkId != -1) {
        // Connected to a Wi-Fi network
        val ssid = wifiInfo.ssid
        return ssid ?: ""
    } else {
        // Not connected to a Wi-Fi network
        return ""
    }
}

fun isWifiConnectedWithInternet(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

fun Context.connectToWifi(
    ssid: String,
    pass: String,
    nwtCallback: ConnectivityManager.NetworkCallback
) {

    val wifiNetworkSpecifier = WifiNetworkSpecifier.Builder()
        .setSsid(ssid)
        .setWpa2Passphrase(pass)
        .build()

    val request = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .setNetworkSpecifier(wifiNetworkSpecifier)
        .build()

    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

//    val nwtCallback = object : ConnectivityManager.NetworkCallback() {
//        override fun onAvailable(network: Network) {
//            RLog.d("duongnk", "onAvailable")
//
//        }
//
//        override fun onUnavailable() {
//            RLog.d("duongnk", "onUnavailable")
//
//        }
//
//    }
    connectivityManager.bindProcessToNetwork(null)
    connectivityManager.requestNetwork(request, nwtCallback)
//    connectivityManager.unregisterNetworkCallback(nwtCallback)

}

/**
 * Điện thoại phải kết nối tới access point của ESP8266 mở ra để gửi gói tin UDP tới ESP
 * Trường hợp thiết bị dùng kết nối 4G thì phải ngắt kết nối 4G vì wifi của ESP là không kết nối internet
 * -> thiết bị sẽ tự động sử dụng 4G để gửi UDP dẫn đến gói tin không đến được ESP
 */
fun sendDataToESP8266(
    data: String,
    ipAddress: String,
    port: Int,
    onReceived: (Boolean) -> Unit,
    onError: (mess: String) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            RLog.e("Send Data", "data = $data")

            val socket = DatagramSocket()
            val message = data.toByteArray()
            val packet =
                DatagramPacket(message, message.size, InetAddress.getByName(ipAddress), port)

            //gửi gói tin
            socket.send(packet)
            RLog.e("UDP", "sending data..")


            // Nhận gói tin trả lời
            val receivePacket = DatagramPacket(ByteArray(1024), 1024)
            socket.receive(receivePacket)
            val receivedMessage = String(receivePacket.data, 0, receivePacket.length)
            RLog.d("UDP", "Received: $receivedMessage")
            withContext(Dispatchers.Main) {
                onReceived(receivedMessage == "ryuuntaesp_code_success")
            }

            val startTime = System.currentTimeMillis()
//            while (System.currentTimeMillis() - startTime < 60000) {    //chờ trong 60s
//
////                Thread.sleep(500)
//                if (receivedMessage == "ryuuntaesp success" || receivedMessage == "ryuuntaesp failure") {
//                    break
//                }
//            }

            socket.close()

        } catch (e: Exception) {
            // Xử lý lỗi
            e.printStackTrace()
            RLog.e("UDP", "Error sending data: ${e.message}")
            withContext(Dispatchers.Main) {
                onError("Error sending data: ${e.message}")

            }

        }
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