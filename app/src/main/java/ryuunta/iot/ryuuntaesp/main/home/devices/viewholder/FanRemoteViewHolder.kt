package ryuunta.iot.ryuuntaesp.main.home.devices.viewholder

import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.core.base.RViewHolder
import ryuunta.iot.ryuuntaesp.data.model.DeviceItem
import ryuunta.iot.ryuuntaesp.data.model.RItem
import ryuunta.iot.ryuuntaesp.databinding.ItemFanRemoteBinding
import ryuunta.iot.ryuuntaesp.helper.ControlHelper

class FanRemoteViewHolder(val binding: ItemFanRemoteBinding) : RViewHolder<RItem>(binding.root) {

    private val controlHelper = ControlHelper()
    private var state = 0

    init {
        binding.apply {
            btnQuickSwitch.setOnClickListener {
                currentItem?.let { item ->
                    btnQuickSwitch.isClickable = false
                    controlHelper.controlDevice(
                        (item as DeviceItem.FanRemote).device.id,
                        item.device.buttonList.filterKeys { it.startsWith("1") },       //select id start with 1 it means button 1 (fan level 1)
                        state == 1, onStateUpdated = { mapElm ->
                            val elm = mapElm.values.first()
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
        val fanRemote = item as DeviceItem.FanRemote
        binding.apply {
            txtDeviceLabel.text = getString(fanRemote.resLabel)
            imgDeviceIcon.setImageResource(fanRemote.resIcon)

            controlHelper.controlDevice(fanRemote.device.id,
                item.device.buttonList.filterKeys { it.startsWith("1") },
                null, { mapElm ->
                    val elm = mapElm.values.first()

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