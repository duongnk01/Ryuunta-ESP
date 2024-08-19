package com.ryuunta.iot.esp.utils

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class WiFiAuthenticator {

    private val TAG = "WiFiAuthenticator"

    private val SERVER_IP = "8.8.8.8"
    private val SERVER_PORT = 53

    fun authenticate(ssid: String, password: String): Boolean {
        // Tạo một luồng UDP
        val socket = DatagramSocket()

        // Gửi yêu cầu xác thực
        val request = "CONNECT $ssid $password\r\n".toByteArray()
        val packet = DatagramPacket(request, request.size, InetAddress.getByName(SERVER_IP), SERVER_PORT)
        socket.send(packet)

        // Nhận phản hồi từ máy chủ
        val buffer = ByteArray(1024)
        val responsePacket = DatagramPacket(buffer, buffer.size)
        socket.receive(responsePacket)

        // Đóng luồng UDP
        socket.close()

        // Kiểm tra phản hồi từ máy chủ
        val response = String(buffer, 0, responsePacket.length)
        return response.startsWith("2")
    }

    fun authenticateAsync(ssid: String, password: String): Deferred<Boolean> {
        return GlobalScope.async {
            authenticate(ssid, password)
        }
    }
}