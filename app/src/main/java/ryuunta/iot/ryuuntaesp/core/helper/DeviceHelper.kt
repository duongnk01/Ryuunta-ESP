package ryuunta.iot.ryuuntaesp.core.helper

import ryuunta.iot.ryuuntaesp.data.model.DeviceObj
import ryuunta.iot.ryuuntaesp.data.model.RItem
import ryuunta.iot.ryuuntaesp.home.devices.DeviceViewType

class DeviceHelper {

    private var _rItem: RItem? = null

    private var rItem : RItem?
        get() = _rItem
        set(value) {
            _rItem = value
        }

    fun getAllDevices(): List<DeviceObj> = listOf(
        DeviceObj(0, "Điều khiển quạt", "fan_remote", DeviceViewType.FAN_REMOTE, hashMapOf(0 to "swing", 1 to "level1", 2 to "level2")),
        DeviceObj(1, "Công tắc", "switch_button", DeviceViewType.SWITCH_BUTTON, hashMapOf(0 to "button1")),
        DeviceObj(2, "Công tắc 3 nút", "fan_remote", DeviceViewType.SWITCH_BUTTON, hashMapOf(0 to "swing", 1 to "level1", 2 to "level2")),
//        DeviceObj(3, "Công tắc 4 nút", "switch_4_button", DeviceViewType.SWITCH_BUTTON, hashMapOf(0 to "button1", 1 to "button2", 2 to "button3", 3 to "button4")),
//        DeviceObj(4, "Công tắc 5 nút", "switch_5_button", DeviceViewType.SWITCH_BUTTON, hashMapOf(0 to "button1", 1 to "button2", 2 to "button3", 3 to "button4", 4 to "button5")),
//        DeviceObj(5, "Công tắc 6 nút", "switch_6_button", DeviceViewType.SWITCH_BUTTON, hashMapOf(0 to "button1", 1 to "button2", 2 to "button3", 3 to "button4", 4 to "button5", 5 to "button6")),
//        DeviceObj(6, "Công tắc 6 nút", "switch_6_button", DeviceViewType.SWITCH_BUTTON, hashMapOf(0 to "button1", 1 to "button2", 2 to "button3", 3 to "button4", 4 to "button5", 5 to "button6")),
//        DeviceObj(7, "Công tắc 6 nút", "switch_6_button", DeviceViewType.SWITCH_BUTTON, hashMapOf(0 to "button1", 1 to "button2", 2 to "button3", 3 to "button4", 4 to "button5", 5 to "button6")),
//        DeviceObj(8, "Công tắc 6 nút", "switch_6_button", DeviceViewType.SWITCH_BUTTON, hashMapOf(0 to "button1", 1 to "button2", 2 to "button3", 3 to "button4", 4 to "button5", 5 to "button6")),
//        DeviceObj(9, "Công tắc 6 nút", "switch_6_button", DeviceViewType.SWITCH_BUTTON, hashMapOf(0 to "button1", 1 to "button2", 2 to "button3", 3 to "button4", 4 to "button5", 5 to "button6")),
//        DeviceObj(10, "Công tắc 6 nút", "switch_6_button", DeviceViewType.SWITCH_BUTTON, hashMapOf(0 to "button1", 1 to "button2", 2 to "button3", 3 to "button4", 4 to "button5", 5 to "button6")),
    )

    fun getDeviceById(deviceId: Int): DeviceObj? {
        try {
            val device =  getAllDevices().single { it.id == deviceId }
            return device
        } catch (e : Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getRItemArgs() : RItem? = _rItem


}