package ryuunta.iot.ryuuntaesp.main.devices

import android.content.Context
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.data.model.ItemDeviceType
import ryuunta.iot.ryuuntaesp.main.home.devices.DeviceViewType

object ResourceFactory {

    private val listDeviceType = listOf(
        ItemDeviceType(DeviceViewType.SWITCH_BUTTON.code),
        ItemDeviceType(DeviceViewType.SWITCH_4_BUTTON.code),
        ItemDeviceType(DeviceViewType.FAN_REMOTE.code),
        ItemDeviceType(DeviceViewType.DOOR_LOCK.code)
    )

    fun getListDeviceType() = listDeviceType

    fun getResByViewType(viewType: Int): Int {
        return when (viewType) {
            DeviceViewType.SWITCH_BUTTON.code -> R.drawable.ic_switch_single_button
            DeviceViewType.SWITCH_4_BUTTON.code -> R.drawable.ic_light_switch_button
            DeviceViewType.FAN_REMOTE.code -> R.drawable.ic_fan
            DeviceViewType.DOOR_LOCK.code -> R.drawable.ic_door_lock
            else -> R.drawable.ic_warning
        }
    }

    fun Context.getNameByViewType(viewType: Int): String {
        return when (viewType) {
            DeviceViewType.SWITCH_BUTTON.code -> getString(R.string.txt_switch_1_button)
            DeviceViewType.SWITCH_4_BUTTON.code -> getString(R.string.txt_switch_4_button)
            DeviceViewType.FAN_REMOTE.code -> getString(R.string.txt_fan_remote)
            DeviceViewType.DOOR_LOCK.code -> getString(R.string.txt_door_lock)
            else -> getString(R.string.unknown)
        }
    }
}