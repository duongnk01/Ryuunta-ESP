package ryuunta.iot.ryuuntaesp.home.devices

val listDeviceType = listOf(
    DeviceViewType.SWITCH_BUTTON,
    DeviceViewType.FAN_REMOTE
)
enum class DeviceViewType(val code: Int) {
    SWITCH_BUTTON(0), SWITCH_4_BUTTON(1), FAN_REMOTE(2)
}