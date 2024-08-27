package ryuunta.iot.ryuuntaesp.data.model

import ryuunta.iot.ryuuntaesp.main.home.devices.DeviceViewType

data class DeviceObj(
    val id: Int = 0,
    val label: String = "",
    val devPath: String = "",
    val type: DeviceViewType,
    val buttonList: HashMap<Int, String> = hashMapOf()
)