package ryuunta.iot.ryuuntaesp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import ryuunta.iot.ryuuntaesp.data.model.HouseObj
import ryuunta.iot.ryuuntaesp.databinding.ItemSpinHomeBinding
import ryuunta.iot.ryuuntaesp.databinding.SpinHomeBinding
import ryuunta.iot.ryuuntaesp.utils.show

class HomeSpinnerAdapter(
    val context: Context,
) : BaseAdapter() {

    private val _listHomeUser: MutableList<HouseObj> = mutableListOf()

    var listHomeUser: List<HouseObj>
        get() = _listHomeUser
        set(value) {
            _listHomeUser.clear()
            _listHomeUser.addAll(value)
        }

    private var currentPosHomeSelected: Int = 0

    var currentPosSelected: Int
        get() = currentPosHomeSelected
        set(value) {
            currentPosHomeSelected = value
        }
    override fun getCount(): Int {
        return _listHomeUser.size
    }

    override fun getItem(position: Int): HouseObj {
        return _listHomeUser[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    /**
     * function này xử lý cho view bên ngoài của spinner
     */
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v = SpinHomeBinding.inflate(LayoutInflater.from(context), parent, false)
        if (position == _listHomeUser.size) {
//            v.imgHomeAvatar.setImageResource(R.drawable.baseline_add_home_24)
//            v.txtHomeName.text = context.getString(R.string.add_more_home)
        } else {
//            v.imgHomeAvatar.setImageResource(_listHomeUser[position].icon)
            v.txtHomeName.text = _listHomeUser[position].name
        }
//        if (getItem(position) == homeSelected) {
//            v.imgHomeSelected.visibility = View.VISIBLE
//        }

        return v.root
    }

    /**
     * function này xử lý vỉew trong dropdown của spinner
     */
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v = ItemSpinHomeBinding.inflate(LayoutInflater.from(context), parent, false)

//        if (position == listHomeUser.size) {
//            v.imgHomeAvatar.setImageResource(R.drawable.sharp_add_home_24)
//            v.txtHomeName.visibility = View.GONE
//        } else {
//
//        }
//        v.imgHomeAvatar.setImageResource(_listHomeUser[position].icon)
        v.txtHomeName.text = _listHomeUser[position].name
        if (position == currentPosSelected) {
            v.imgHomeSelected.show()
        }

        return v.root
    }

}