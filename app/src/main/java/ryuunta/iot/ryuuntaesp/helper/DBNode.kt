package ryuunta.iot.ryuuntaesp.helper

enum class DBNode(val path: String) {
    USERS("users"),
    DEVICES("devices"),
    BUTTON_LIST("buttonList"),
    HOUSES("houses"),
    ROOMS("rooms"),
    DEVICES_ID_SIGNED("devicesIdSigned")

}