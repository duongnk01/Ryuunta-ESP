package ryuunta.iot.ryuuntaesp.main.home.content

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.core.base.RViewHolder
import ryuunta.iot.ryuuntaesp.helper.ControlHelper
import ryuunta.iot.ryuuntaesp.data.model.DeviceElement
import ryuunta.iot.ryuuntaesp.databinding.ItemDeviceElementBinding
import ryuunta.iot.ryuuntaesp.utils.DeviceElementDiff
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick

class DeviceElementsAdapter(
    private val onElementClick: (DeviceElement) -> Unit
) : ListAdapter<DeviceElement, RViewHolder<DeviceElement>>(DeviceElementDiff()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RViewHolder<DeviceElement> {
        val v = ItemDeviceElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeviceElementsVH(v)
    }

    override fun onBindViewHolder(holder: RViewHolder<DeviceElement>, position: Int) {
        holder.onBind(getItem(position), position)
    }

    override fun onBindViewHolder(
        holder: RViewHolder<DeviceElement>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            payloads.forEach {
                if (it is DeviceElement) {
                    holder.onBind(getItem(position))
                }
            }
        }
    }


    inner class DeviceElementsVH(val binding: ItemDeviceElementBinding) :
        RViewHolder<DeviceElement>(binding.root) {

        private val controlHelper = ControlHelper()
        private val states = MutableList(currentList.size) { false}

        init {
            binding.apply {
                root.setPreventDoubleClick {
                    currentItem?.let { item ->
                        root.isClickable = false
                        controlHelper.controlDevice(
                            item.devicePath,
                            listOf(item.elmPath),
                            states[currentPosition])
                             { elmPath, isOn ->
                                root.isClickable = true
                                if (!isOn) {
                                    imgPowerState.setImageResource(R.drawable.ic_power_switch_off)
                                } else {
                                    imgPowerState.setImageResource(R.drawable.ic_power_switch_on)
                                }
                                states[currentPosition] = isOn
                            }

                    }
                }
            }

        }

        override fun onBind(item: DeviceElement) {
            super.onBind(item)
            binding.apply {
                btnElement.text = item.elmPath
                controlHelper.controlDevice(item.devicePath,
                    listOf(item.elmPath),
                    null)
                    { elmPath, isOn ->
                        states[currentPosition] = isOn
                        if (!isOn) {
                            imgPowerState.setImageResource(R.drawable.ic_power_switch_off)
                        } else {
                            imgPowerState.setImageResource(R.drawable.ic_power_switch_on)
                        }
                    }
            }
        }

    }
}