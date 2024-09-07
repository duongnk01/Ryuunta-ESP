package ryuunta.iot.ryuuntaesp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.core.base.RViewHolder
import ryuunta.iot.ryuuntaesp.data.model.HouseObj
import ryuunta.iot.ryuuntaesp.databinding.ItemHouseManageBinding
import ryuunta.iot.ryuuntaesp.utils.HouseManageDiff
import ryuunta.iot.ryuuntaesp.utils.getRandomSticker
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick

class HouseManageAdapter(
    private val onItemClick: (HouseObj) -> Unit
) : ListAdapter<HouseObj, RViewHolder<HouseObj>>(HouseManageDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RViewHolder<HouseObj> {
        val v = ItemHouseManageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HouseManageVH(v)
    }

    override fun onBindViewHolder(holder: RViewHolder<HouseObj>, position: Int) {
        holder.onBind(getItem(position), onItemClick)
    }

    inner class HouseManageVH(val binding: ItemHouseManageBinding) : RViewHolder<HouseObj>(binding.root) {
        override fun onBind(item: HouseObj) {

            getRandomSticker(context, "sticker", binding.imgBgItemHouse)
            binding.txtNameHouse.text = item.name
            binding.txtNumRoom.text = "${item.rooms.size} ${context.getString(R.string.room)}"
            binding.txtNumDevice.text = "${item.devices.size} ${context.getString(R.string.device)}"

            binding.btnItemHouse.setPreventDoubleClick {
                onItemClick(item)
            }

        }
    }
}