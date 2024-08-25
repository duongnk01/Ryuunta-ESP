package ryuunta.iot.ryuuntaesp.utils

import androidx.recyclerview.widget.DiffUtil
import ryuunta.iot.ryuuntaesp.data.model.DeviceElement
import ryuunta.iot.ryuuntaesp.data.model.RItem

open class UIDiff<T> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T & Any, newItem: T & Any): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: T & Any, newItem: T & Any): Boolean {
        return oldItem == newItem
    }


}

class DeviceHomeDiff : UIDiff<RItem>() {
    override fun areItemsTheSame(oldItem: RItem, newItem: RItem): Boolean {
        return oldItem.viewType == newItem.viewType
    }

    override fun areContentsTheSame(oldItem: RItem, newItem: RItem): Boolean {
        return oldItem.viewType == newItem.viewType
    }

}

class DeviceElementDiff : UIDiff<DeviceElement>() {

    override fun areItemsTheSame(oldItem: DeviceElement, newItem: DeviceElement): Boolean {
        return oldItem.elmPath == newItem.elmPath

    }

    override fun areContentsTheSame(oldItem: DeviceElement, newItem: DeviceElement): Boolean {
        return oldItem.elmPath == newItem.elmPath

    }

}