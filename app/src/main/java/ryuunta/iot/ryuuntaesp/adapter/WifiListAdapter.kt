package ryuunta.iot.ryuuntaesp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ryuunta.iot.ryuuntaesp.data.model.WifiSSID
import ryuunta.iot.ryuuntaesp.databinding.ItemSsidBinding

class WifiListAdapter(
    private val onItemClick: (wifi: WifiSSID) -> Unit
) : ListAdapter<WifiSSID, WifiListAdapter.WifiViewHolder>(
    object : DiffUtil.ItemCallback<WifiSSID>() {
        override fun areItemsTheSame(oldItem: WifiSSID, newItem: WifiSSID): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WifiSSID, newItem: WifiSSID): Boolean {
            return oldItem == newItem
        }

    }
) {
     class WifiViewHolder constructor(val binding: ItemSsidBinding): ViewHolder(binding.root) {
         fun onBind(item: WifiSSID, onItemClick: (wifi: WifiSSID) -> Unit) {
             binding.ssid = item.ssid
             binding.rssi = item.level
             binding.lnSsid.setOnClickListener {
                 onItemClick(item)
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