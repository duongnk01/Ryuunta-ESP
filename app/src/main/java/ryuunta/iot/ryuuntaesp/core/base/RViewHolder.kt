package ryuunta.iot.ryuuntaesp.core.base

import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick

abstract class RViewHolder<T>(v: View) : RecyclerView.ViewHolder(v) {

    open var onItemClick: (T) -> Unit = {}
    protected var currentItem: T? = null
    protected var currentPosition = -1

    val context: Context
        get() = itemView.context

    init {
        v.setPreventDoubleClick {
            currentItem?.let(onItemClick)

        }
    }

    protected fun getString(@StringRes resId: Int) = itemView.context.getString(resId)

    open fun onBind(item: T) {
        currentItem = item
    }

    open fun onBind(item: T, onItemClick: (T) -> Unit) {
        this.onItemClick = onItemClick
        onBind(item)

    }

    open fun onBind(item: T, position: Int, onItemClick: (T) -> Unit) {
        currentPosition = position
        this.onItemClick = onItemClick
        onBind(item, position)
    }

    open fun onBind(item: T, position: Int) {
        currentPosition = position
        onBind(item)
    }
}