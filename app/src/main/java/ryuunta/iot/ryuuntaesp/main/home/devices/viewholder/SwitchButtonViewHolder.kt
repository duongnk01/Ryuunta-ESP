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
    private var state = 0

    init {
        binding.apply {
            btnQuickSwitch.setOnClickListener {
                currentItem?.let { item ->
                    btnQuickSwitch.isClickable = false
                    controlHelper.controlDevice(
                        (item as DeviceItem.SwitchButton).device.id,
                        item.device.buttonList,
                        state == 1, onStateUpdated = { elm ->
                            btnQuickSwitch.isClickable = true
                            if (elm.value == 0) {
                                btnQuickSwitch.setImageResource(R.drawable.ic_power_switch_off)
                            } else {
                                btnQuickSwitch.setImageResource(R.drawable.ic_power_switch_on)
                            }
                            state = if (elm.value == 1) 0 else 1
                        }, onError = { code, message ->
                            btnQuickSwitch.isClickable = true
                        })

                }


            }
        }
    }

    override fun onBind(item: RItem) {
        super.onBind(item)
        val switchButton = item as DeviceItem.SwitchButton
        binding.apply {
            txtDeviceLabel.text = getString(switchButton.resLabel)
            imgDeviceIcon.setImageResource(switchButton.resIcon)

            controlHelper.controlDevice(switchButton.device.id,
                switchButton.device.buttonList, null, { elm ->
                    state = elm.value
                    if (state == 0) {
                        btnQuickSwitch.setImageResource(R.drawable.ic_power_switch_off)
                    } else {
                        btnQuickSwitch.setImageResource(R.drawable.ic_power_switch_on)
                    }
                }, onError = { code, message ->
                })
        }
    }
}