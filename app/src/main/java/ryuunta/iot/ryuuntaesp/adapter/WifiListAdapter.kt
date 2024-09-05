package ryuunta.iot.ryuuntaesp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.data.model.WifiSSID
import ryuunta.iot.ryuuntaesp.databinding.ItemSsidBinding
import java.util.Locale

class WifiListAdapter(
    private val onItemClick: (wifi: WifiSSID) -> Unit
) : ListAdapter<WifiSSID, WifiListAdapter.WifiViewHolder>(
    object : DiffUtil.ItemCallback<WifiSSID>() {
        override fun areItemsTheSame(oldItem: WifiSSID, newItem: WifiSSID): Boolean {
            return oldItem.ssid == newItem.ssid
        }

        override fun areContentsTheSame(oldItem: WifiSSID, newItem: WifiSSID): Boolean {
            return oldItem == newItem
        }

    }
) {
    class WifiViewHolder(val binding: ItemSsidBinding) : ViewHolder(binding.root) {
        fun onBind(item: WifiSSID, onItemClick: (wifi: WifiSSID) -> Unit) {
            binding.apply {
                txtSsid.text = item.ssid
                lnSsid.setOnClickListener {
                    onItemClick(item)
                }
                txtRssi.text = if (item.level < -80) {
                    txtRssi.setTextColor(ContextCompat.getColor(itemView.context, R.color.red_error))
                    "Rất yếu"
                } else if (item.level < -60) {
                    txtRssi.setTextColor(ContextCompat.getColor(itemView.context, R.color.pastel_peach_1))
                    "Yếu"
                } else if (item.level < -40){
                    txtRssi.setTextColor(ContextCompat.getColor(itemView.context, R.color.pastel_orange_1))
                    "Trung bình"
                } else {
                    txtRssi.setTextColor(ContextCompat.getColor(itemView.context, R.color.pastel_green_1))
                    "Tốt"
                }

            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WifiViewHolder {
        val v = ItemSsidBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WifiViewHolder(v)
    }

    override fun onBindViewHolder(holder: WifiViewHolder, position: Int) {
        holder.onBind(getItem(position), onItemClick)
    }
}