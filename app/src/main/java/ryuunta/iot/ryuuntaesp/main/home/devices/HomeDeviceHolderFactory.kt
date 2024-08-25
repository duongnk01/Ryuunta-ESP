package ryuunta.iot.ryuuntaesp.main.home.devices

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ryuunta.iot.ryuuntaesp.core.base.RViewHolder
import ryuunta.iot.ryuuntaesp.data.model.DeviceItem
import ryuunta.iot.ryuuntaesp.data.model.RItem
import ryuunta.iot.ryuuntaesp.databinding.ItemFanRemoteBinding
import ryuunta.iot.ryuuntaesp.databinding.ItemSwitchButtonBinding
import ryuunta.iot.ryuuntaesp.main.home.devices.viewholder.FanRemoteViewHolder
import ryuunta.iot.ryuuntaesp.main.home.devices.viewholder.SwitchButtonViewHolder

object HomeDeviceHolderFactory {
    private fun getItemFullSize(rootView: View, isFullSpan: Boolean = true) {
        val lp: StaggeredGridLayoutManager.LayoutParams =
            rootView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        lp.isFullSpan = isFullSpan
    }

    fun getViewHolder(typeCode: Int, parent: ViewGroup) :RViewHolder<RItem> {
        return when (typeCode) {
            DeviceViewType.SWITCH_BUTTON.code -> {
                val binding = ItemSwitchButtonBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                SwitchButtonViewHolder(binding)
            }
            DeviceViewType.FAN_REMOTE.code -> {
                val binding = ItemFanRemoteBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                FanRemoteViewHolder(binding)
            }
            else -> {
                throw Exception("Unknown device type detected")
            }
        }
    }

    fun getViewType(rItem: RItem): Int {
        return when(rItem) {
            is DeviceItem.SwitchButton -> DeviceViewType.SWITCH_BUTTON.code
            is DeviceItem.FanRemote -> DeviceViewType.FAN_REMOTE.code
            else -> -1

        }
    }
}