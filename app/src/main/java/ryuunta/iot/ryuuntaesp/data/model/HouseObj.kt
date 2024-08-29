package ryuunta.iot.ryuuntaesp.data.model

data class HouseObj(
    var id: String = "",
    var name: String = "",
    var rooms: Map<String, RoomObj> = mapOf(),
    var devices: Map<String, DeviceObj> = mapOf(),
) {
    class RoomObj(
        var id: String = "",
        var name: String = "",
        var deviceIdsSigned: Map<String, String> = mapOf()
    )
}
