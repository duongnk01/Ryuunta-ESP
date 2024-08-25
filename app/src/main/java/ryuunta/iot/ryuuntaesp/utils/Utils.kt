package ryuunta.iot.ryuuntaesp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.wifi.WifiManager
import android.widget.ImageView
import com.google.gson.Gson
import ryuunta.iot.ryuuntaesp.data.model.WifiSSID
import java.util.Random

@SuppressLint("MissingPermission")
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

fun <K, V> splitHashMap(map: Map<K, V>, chunkSize: Int): List<Map<K, V>> {
    val list = map.toList()
    val subLists = list.chunked(chunkSize)
    return subLists.map { it.toMap() }
}

fun getRandomSticker(context: Context, assetPath : String, iv: ImageView) {
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

fun getAssetBackground(context: Context, assetName : String, iv: ImageView) {
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