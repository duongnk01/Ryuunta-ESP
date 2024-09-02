package ryuunta.iot.ryuuntaesp.main.home.devices.viewholder

import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.core.base.RViewHolder
import ryuunta.iot.ryuuntaesp.data.model.DeviceItem
import ryuunta.iot.ryuuntaesp.data.model.RItem
import ryuunta.iot.ryuuntaesp.databinding.ItemSwitchButtonBinding
import ryuunta.iot.ryuuntaesp.helper.ControlHelper

class SwitchButtonViewHolder(val binding: ItemSwitchButtonBinding) :
    RViewHolder<RItem>(binding.root) {

    private val controlHelper = ControlHelper()

    init {
        binding.apply {
        }
    }

    override fun onBind(item: RItem) {
        super.onBind(item)
        val switchButton = item as DeviceItem.SwitchButton
        var state = false
        binding.apply {
            if (switchButton.device.label.isNotEmpty()) {
                txtDeviceLabel.text = switchButton.device.label
            } else {
                txtDeviceLabel.text = getString(switchButton.resLabel)
            }
            imgDeviceIcon.setImageResource(switchButton.resIcon)

            controlHelper.controlDevice(switchButton.device.id,
                switchButton.device.buttonList, null, { mapElm ->
                    val elm = mapElm.values.first()
                    state = elm.value == 1
                    if (state) {
                        btnQuickSwitch.setImageResource(R.drawable.ic_power_switch_on)
                    } else {
                        btnQuickSwitch.setImageResource(R.drawable.ic_power_switch_off)
                    }
                }, onError = { code, message ->
                })

            btnQuickSwitch.setOnClickListener {
                btnQuickSwitch.isClickable = false
                controlHelper.controlDevice(
                    switchButton.device.id,
                    switchButton.device.buttonList,
                    !state, onStateUpdated = { mapElm ->
                        val elm = mapElm.values.first()
                        btnQuickSwitch.isClickable = true
                        if (elm.value == 0) {
                            btnQuickSwitch.setImageResource(R.drawable.ic_power_switch_off)
                        } else {
                            btnQuickSwitch.setImageResource(R.drawable.ic_power_switch_on)
                        }
                    }, onError = { code, message ->
                        btnQuickSwitch.isClickable = true
                    })

            }

        }
    }
}