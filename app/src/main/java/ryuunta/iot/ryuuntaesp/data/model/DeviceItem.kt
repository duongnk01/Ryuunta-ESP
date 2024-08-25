package ryuunta.iot.ryuuntaesp.data.model

sealed class DeviceItem(
    viewType: Int,
    val resIcon: Int = -1,
    val resLabel: Int = -1,
    val device: DeviceObj
) : RItem(viewType) {

    class SwitchButton(
        viewType: Int,
        resIcon: Int,
        resLabel: Int,
        device: DeviceObj
    ) : DeviceItem(viewType, resIcon, resLabel, device)

    class Switch4Button(
        viewType: Int,
        resIcon: Int,
        resLabel: Int,
        device: DeviceObj
    ) : DeviceItem(viewType, resIcon, resLabel, device)

    class FanRemote(
        viewType: Int,
        resIcon: Int,
        resLabel: Int,
        device: DeviceObj
    ) : DeviceItem(viewType, resIcon, resLabel, device)
}
