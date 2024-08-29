package ryuunta.iot.ryuuntaesp.main.home.content

import ryuunta.iot.ryuuntaesp.core.base.BaseViewModel
import ryuunta.iot.ryuuntaesp.helper.DeviceHelper
import ryuunta.iot.ryuuntaesp.helper.GroupHelper

class DevicesViewModel : BaseViewModel() {

    private val groupHelper = GroupHelper()
    private val deviceHelper = DeviceHelper()

    fun getRoomName(roomId: String, onCompleted: (String) -> Unit) {
        groupHelper.getRoomName(roomId) {
            onCompleted(it)
        }

    }

    fun getDeviceName(deviceId: String, onCompleted: (String) -> Unit) {
        deviceHelper.getDeviceName(deviceId, onCompleted)
    }
}