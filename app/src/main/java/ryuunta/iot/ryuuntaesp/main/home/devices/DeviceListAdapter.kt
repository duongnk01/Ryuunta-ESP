package ryuunta.iot.ryuuntaesp.main.home.devices

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ryuunta.iot.ryuuntaesp.core.base.RViewHolder
import ryuunta.iot.ryuuntaesp.data.model.RItem
import ryuunta.iot.ryuuntaesp.utils.DeviceHomeDiff

class DeviceListAdapter(
    private val onItemClick: (RItem) -> Unit,
    private val onAdapterEmpty: (Boolean) -> Unit
) : ListAdapter<RItem, RViewHolder<RItem>>(DeviceHomeDiff()) {

    override fun getItemCount(): Int {
        onAdapterEmpty(currentList.isEmpty())
        return super.getItemCount()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RViewHolder<RItem> {
        return HomeDeviceHolderFactory.getViewHolder(viewType, parent)
    }

    override fun onBindViewHolder(holder: RViewHolder<RItem>, position: Int) {
        holder.onBind(getItem(position), onItemClick)
    }

    override fun getItemViewType(position: Int): Int {
        return HomeDeviceHolderFactory.getViewType(getItem(position))
    }

    override fun onBindViewHolder(
        holder: RViewHolder<RItem>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            payloads.forEach {
                if (it is RItem) {
                    holder.onBind(getItem(position))
                }
            }
        }
    }

}