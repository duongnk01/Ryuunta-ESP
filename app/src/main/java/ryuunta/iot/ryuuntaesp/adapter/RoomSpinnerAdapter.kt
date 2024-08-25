package ryuunta.iot.ryuuntaesp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.data.model.RoomObj
import ryuunta.iot.ryuuntaesp.databinding.ItemSpinRoomBinding
import ryuunta.iot.ryuuntaesp.databinding.SpinRoomBinding
import ryuunta.iot.ryuuntaesp.utils.gone

class RoomSpinnerAdapter (
    private val context: Context,
    private val listRoom: List<RoomObj>
) : BaseAdapter(){

    private var posRoomSelected: Int = 0

    var currRoomSelectedPosition: Int
        get() = posRoomSelected
        set(value) {
            posRoomSelected = value
        }

    override fun getCount(): Int {
        return listRoom.size
    }

    override fun getItem(position: Int): RoomObj {
        return listRoom[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v = SpinRoomBinding.inflate(LayoutInflater.from(context), parent, false)
        if (position != listRoom.size) {
            v.txtRoomName.text = listRoom[position].name
        }
        return v.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v = ItemSpinRoomBinding.inflate(LayoutInflater.from(context), parent, false)
        when (position) {
            0 -> {
                v.imgShowMore.visibility = View.VISIBLE
                v.txtRoomName.text = listRoom[position].name
            }
            listRoom.size - 1 -> {
                v.baseLine.gone()
            }
            else -> {
                v.txtRoomName.text = listRoom[position].name
            }
        }

        return v.root
    }
}