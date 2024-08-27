package ryuunta.iot.ryuuntaesp.utils

import androidx.recyclerview.widget.DiffUtil
import ryuunta.iot.ryuuntaesp.data.model.DeviceItem
import ryuunta.iot.ryuuntaesp.data.model.RItem
import ryuunta.iot.ryuuntaesp.data.model.ScenarioItem

open class UIDiff : DiffUtil.ItemCallback<RItem>() {
    override fun areItemsTheSame(oldItem: RItem, newItem: RItem): Boolean {
        return oldItem.viewType == newItem.viewType
    }

    override fun areContentsTheSame(oldItem: RItem, newItem: RItem): Boolean {
        return oldItem.viewType == newItem.viewType
    }


}

class DeviceHomeDiff : UIDiff() {
    override fun areItemsTheSame(oldItem: RItem, newItem: RItem): Boolean {
        if (oldItem is DeviceItem && newItem is DeviceItem) {
            return oldItem.device.id == newItem.device.id
        }
        return super.areItemsTheSame(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItem: RItem, newItem: RItem): Boolean {
        if (oldItem is DeviceItem && newItem is DeviceItem) {
            return oldItem.device.label == newItem.device.label
        }
        return super.areContentsTheSame(oldItem, newItem)
    }

}


class ScenarioItemDiff : DiffUtil.ItemCallback<ScenarioItem>() {
    override fun areItemsTheSame(oldItem: ScenarioItem, newItem: ScenarioItem): Boolean {
        if (oldItem is ScenarioItem.QuickScenario && newItem is ScenarioItem.QuickScenario) {
            return oldItem.label == newItem.label
        }
        return oldItem.viewType == newItem.viewType
    }

    override fun areContentsTheSame(oldItem: ScenarioItem, newItem: ScenarioItem): Boolean {
        if (oldItem is ScenarioItem.QuickScenario && newItem is ScenarioItem.QuickScenario) {
            return (oldItem.label + oldItem.bgColor.toString()) == newItem.label + newItem.bgColor.toString()
        }
        return oldItem.viewType == newItem.viewType
    }
}