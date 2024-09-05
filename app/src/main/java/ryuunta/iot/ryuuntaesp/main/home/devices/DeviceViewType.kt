package ryuunta.iot.ryuuntaesp.main.home.devices

val listDeviceType = listOf(
    DeviceViewType.SWITCH_BUTTON,
    DeviceViewType.SWITCH_4_BUTTON,
    DeviceViewType.SWITCH_3_BUTTON,
    DeviceViewType.FAN_REMOTE,
    DeviceViewType.DOOR_LOCK
)
enum class DeviceViewType(val code: Int) {
    SWITCH_BUTTON(0), SWITCH_4_BUTTON(1), SWITCH_3_BUTTON(2), FAN_REMOTE(3), DOOR_LOCK(4)
}