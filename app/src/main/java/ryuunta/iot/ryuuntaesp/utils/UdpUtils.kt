package ryuunta.iot.ryuuntaesp.utils

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class UdpUtils(
    private val ipAddress: String,
    private val port: Int
) {
    private val socket = DatagramSocket()
    private var message: ByteArray = ByteArray(1024)
    private val receivePacket = DatagramPacket(ByteArray(1024), 1024)
    private lateinit var dataPacket: DatagramPacket

    var data: String = ""
        set(value) {
            message = value.toByteArray()
            dataPacket =
                DatagramPacket(message, message.size, InetAddress.getByName(ipAddress), port)
            field = value
        }



    fun sendUDP() {
        try {
            RLog.e("Sending", "data = $data")
            socket.send(dataPacket)
        } catch (e: Exception) {
            // Xử lý lỗi
            e.printStackTrace()
            RLog.e("UDP", "Error sending data: ${e.message}")
        }
    }

    fun receiveUDP() {
        socket.receive(receivePacket)
        data = String(receivePacket.data, 0, receivePacket.length)

    }
}