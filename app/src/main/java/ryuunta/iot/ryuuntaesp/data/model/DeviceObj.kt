package ryuunta.iot.ryuuntaesp.data.model

import ryuunta.iot.ryuuntaesp.main.home.devices.DeviceViewType

data class DeviceObj(
    var id: String = "",
    var label: String = "",
//    var devPath: String = "",
    var type: String = DeviceViewType.SWITCH_BUTTON.name,
    var buttonList: Map<String, ElementInfoObj> = mapOf()
)