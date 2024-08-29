package ryuunta.iot.ryuuntaesp.data.model

data class UserInfo(
    var displayName: String = "",
    var email: String = "",
    var devices: List<RItem> = listOf(),
    var houses: Map<String, HouseObj> = mapOf()

)
