package ryuunta.iot.ryuuntaesp.main.devices

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ryuunta.iot.ryuuntaesp.core.base.RViewHolder
import ryuunta.iot.ryuuntaesp.data.model.ItemDeviceType
import ryuunta.iot.ryuuntaesp.databinding.ItemSwitchButtonBinding
import ryuunta.iot.ryuuntaesp.main.devices.ResourceFactory.getNameByViewType
import ryuunta.iot.ryuuntaesp.main.devices.ResourceFactory.getResByViewType
import ryuunta.iot.ryuuntaesp.utils.gone
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick

class SelectDeviceTypeAdapter(
    private val onItemClick: (ItemDeviceType) -> Unit,
) : ListAdapter<ItemDeviceType, RViewHolder<ItemDeviceType>>(
    object : DiffUtil.ItemCallback<ItemDeviceType>() {
        override fun areItemsTheSame(oldItem: ItemDeviceType, newItem: ItemDeviceType): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: ItemDeviceType, newItem: ItemDeviceType): Boolean {
            return false
        }

    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RViewHolder<ItemDeviceType> {
        val v = ItemSwitchButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectDeviceTypeVH(v)
    }

    override fun onBindViewHolder(holder: RViewHolder<ItemDeviceType>, position: Int) {
        holder.onBind(getItem(position), onItemClick)
    }

    inner class SelectDeviceTypeVH(val binding: ItemSwitchButtonBinding) :
        RViewHolder<ItemDeviceType>(binding.root) {
        init {
            binding.root.setPreventDoubleClick {
                currentItem?.let(onItemClick)
            }
        }

        override fun onBind(item: ItemDeviceType) {
            super.onBind(item)

            binding.apply {
                btnQuickSwitch.gone()
                imgDeviceIcon.setImageResource(getResByViewType(item.deviceType))
                txtDeviceLabel.text = context.getNameByViewType(item.deviceType)
            }
        }


    }
}