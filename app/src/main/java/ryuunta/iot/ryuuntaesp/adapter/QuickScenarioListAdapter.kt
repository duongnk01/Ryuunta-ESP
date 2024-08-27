package ryuunta.iot.ryuuntaesp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import ryuunta.iot.ryuuntaesp.core.base.RViewHolder
import ryuunta.iot.ryuuntaesp.data.model.ScenarioItem
import ryuunta.iot.ryuuntaesp.databinding.ItemQuickScenarioBinding
import ryuunta.iot.ryuuntaesp.utils.ScenarioItemDiff
import ryuunta.iot.ryuuntaesp.utils.getRandomColor
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick

class QuickScenarioListAdapter(
    private val onItemClick: (ScenarioItem) -> Unit
) : ListAdapter<ScenarioItem, QuickScenarioListAdapter.QuickScenarioVH>(ScenarioItemDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuickScenarioVH {
        val v = ItemQuickScenarioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuickScenarioVH(v)
    }

    override fun onBindViewHolder(holder: QuickScenarioVH, position: Int) {
        holder.onBind(getItem(position), onItemClick)
    }

    inner class QuickScenarioVH(val binding: ItemQuickScenarioBinding) :
        RViewHolder<ScenarioItem>(binding.root) {

        init {
            binding.root.setPreventDoubleClick {
                onItemClick(getItem(bindingAdapterPosition))
            }
        }

        override fun onBind(item: ScenarioItem) {
            super.onBind(item)
            val quickScenario = item as ScenarioItem.QuickScenario
            binding.btnQuickScenario.text = quickScenario.label

            try {
                val colorStateList = if (quickScenario.bgColor == null) {
                    ContextCompat.getColorStateList(context, getRandomColor())
                } else {
                    ContextCompat.getColorStateList(context, quickScenario.bgColor)
                }
                binding.btnQuickScenario.backgroundTintList = colorStateList
            } catch (e: Exception) {
                e.printStackTrace()
                binding.btnQuickScenario.backgroundTintList = null
            }


        }
    }


}