package ryuunta.iot.ryuuntaesp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.data.model.HouseObj
import ryuunta.iot.ryuuntaesp.databinding.ItemRoomSelectionBinding
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick

class RoomSelectionAdapter(
    private val onItemSelected: (HouseObj.RoomObj) -> Unit,
) : ListAdapter<HouseObj.RoomObj, RoomSelectionAdapter.RoomSelectionVH>(object : DiffUtil.ItemCallback<HouseObj.RoomObj>() {
    override fun areItemsTheSame(oldItem: HouseObj.RoomObj, newItem: HouseObj.RoomObj): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HouseObj.RoomObj, newItem: HouseObj.RoomObj): Boolean {
        return oldItem.name == newItem.name
    }

}) {


    private var currentSelectedPos = -1

    var selectedPosition: Int
        get() = currentSelectedPos
        set(value) {
            notifyItemChanged(currentSelectedPos)
            currentSelectedPos = value
            notifyItemChanged(currentSelectedPos)
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomSelectionVH {
        val v = ItemRoomSelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RoomSelectionVH(v)
    }

    override fun onBindViewHolder(holder: RoomSelectionVH, position: Int) {
        holder.onBind(getItem(position), onItemSelected)
    }

    inner class RoomSelectionVH(val binding: ItemRoomSelectionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: HouseObj.RoomObj, onItemSelected: (HouseObj.RoomObj) -> Unit) {
            binding.txtRoomName.text = item.name
            if (currentSelectedPos == absoluteAdapterPosition) {
                binding.txtRoomName.setTextColor(Color.WHITE)
                binding.txtRoomName.backgroundTintList = ContextCompat.getColorStateList(itemView.context, R.color.tint_icon_active)
            } else {
                binding.txtRoomName.setTextColor(Color.BLACK)
                binding.txtRoomName.backgroundTintList = null
            }

            binding.txtRoomName.setPreventDoubleClick {
                notifyItemChanged(currentSelectedPos)
                currentSelectedPos = absoluteAdapterPosition
                onItemSelected(item)
                notifyItemChanged(absoluteAdapterPosition)

            }
        }
    }


}