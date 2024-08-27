package ryuunta.iot.ryuuntaesp.data.model

import ryuunta.iot.ryuuntaesp.main.home.devices.DeviceViewType

data class DeviceInforObj(
    val label: String = "",
    val type: String,
    val buttonList: Map<String, ElementInfoObj> = mapOf(),
//    val room: RoomObj? = null
)