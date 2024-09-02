package ryuunta.iot.ryuuntaesp.main.home

import ryuunta.iot.ryuuntaesp.data.model.DeviceItem
import ryuunta.iot.ryuuntaesp.data.model.RItem
import ryuunta.iot.ryuuntaesp.main.RMainFragmentDirections

val HomeFragment.onItemClick: (RItem) -> Unit
    get() = {
        when (it) {
            is DeviceItem.SwitchButton -> {
                val action = RMainFragmentDirections.actionRyuuntaMainFragmentToDevicesFragment(it.device.id, it.resLabel)
                navController.navigate(action)
            }
            is DeviceItem.FanRemote -> {
                val action = RMainFragmentDirections.actionRyuuntaMainFragmentToDevicesFragment(it.device.id, it.resLabel)
                navController.navigate(action)
            }
        }
    }