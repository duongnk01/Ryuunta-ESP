package ryuunta.iot.ryuuntaesp.main.home.devices.viewholder

import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.core.base.RViewHolder
import ryuunta.iot.ryuuntaesp.helper.ControlHelper
import ryuunta.iot.ryuuntaesp.data.model.DeviceItem
import ryuunta.iot.ryuuntaesp.data.model.RItem
import ryuunta.iot.ryuuntaesp.databinding.ItemFanRemoteBinding

class FanRemoteViewHolder(val binding: ItemFanRemoteBinding) : RViewHolder<RItem>(binding.root) {

    private val controlHelper = ControlHelper()
    private var state = false

    init {
        binding.apply {
            btnQuickSwitch.setOnClickListener {
                currentItem?.let { item ->
                    btnQuickSwitch.isClickable = false
                    controlHelper.controlDevice(
                        (item as DeviceItem.FanRemote).device.id,
                        item.device.buttonList,
                        state, onStateUpdated =  { elm ->
                            btnQuickSwitch.isClickable = true
                            if (elm.value == 0) {
                                btnQuickSwitch.setImageResource(R.drawable.ic_power_switch_off)
                            } else {
                                btnQuickSwitch.setImageResource(R.drawable.ic_power_switch_on)
                            }
                            state = elm.value == 1
                        }, onError = { code, message ->
                            btnQuickSwitch.isClickable = true
                        })

                }


            }
        }
    }

    override fun onBind(item: RItem) {
        super.onBind(item)
        val fanRemote = item as DeviceItem.FanRemote
        binding.apply {
            txtDeviceLabel.text = getString(fanRemote.resLabel)
            imgDeviceIcon.setImageResource(fanRemote.resIcon)

//            controlHelper.controlDevice(fanRemote.device.id,
//                fanRemote.device.buttonList, null, { elm ->
//                    state = isOn
//                    if (!isOn) {
//                        btnQuickSwitch.setImageResource(R.drawable.ic_power_switch_off)
//                    } else {
//                        btnQuickSwitch.setImageResource(R.drawable.ic_power_switch_on)
//                    }
//                }, onError = { code, message ->
//                })
        }
    }
}