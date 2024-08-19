package ryuunta.iot.ryuuntaesp.utils

import android.graphics.Rect
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import androidx.annotation.DrawableRes
import androidx.viewbinding.ViewBinding


fun View.isShow() = visibility == View.VISIBLE

fun View.isGone() = visibility == View.GONE

fun View.isInvisible() = visibility == View.INVISIBLE

fun View.show() {
    if (!isShow())
        visibility = View.VISIBLE
}

fun View.show(isGone: Boolean) {
    if (isGone)
        gone()
    else
        show()
}

fun ViewBinding.gone() {
    if (!root.isGone())
        root.visibility = View.GONE
}

fun ViewBinding.show() {
    if (!root.isShow())
        root.visibility = View.VISIBLE
}

fun View.gone() {
    if (!isGone())
        visibility = View.GONE
}

fun View.inv() {
    if (!isInvisible())
        visibility = View.INVISIBLE
}

fun View.setPreventDoubleClick(debounceTime: Long = 300, action: (view: View?) -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View?) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            action(v)
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

fun ViewBinding.setPreventDoubleClick(debounceTime: Long = 500, action: (view: View?) -> Unit) {
    this.root.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View?) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            action(v)
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

fun View.setPreventDoubleClickScaleView(debounceTime: Long = 500, action: () -> Unit) {
    setOnTouchListener(object : View.OnTouchListener {
        private var lastClickTime: Long = 0
        private var rect: Rect? = null

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            fun setScale(scale: Float) {
                v.scaleX = scale
                v.scaleY = scale
            }

            if (event.action == MotionEvent.ACTION_DOWN) {
                //action down: scale view down
                rect = Rect(v.left, v.top, v.right, v.bottom)
                setScale(0.9f)
            } else if (rect != null && !rect!!.contains(
                    v.left + event.x.toInt(),
                    v.top + event.y.toInt()
                )
            ) {
                //action moved out
                setScale(1f)
                return false
            } else if (event.action == MotionEvent.ACTION_UP) {
                //action up
                setScale(1f)
                //handle click too fast
                if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) {
                } else {
                    lastClickTime = SystemClock.elapsedRealtime()
                    action()
                }
            } else {
                //other
            }

            return true
        }
    })
}

fun View.changeBackgroundWhenFocus(
    @DrawableRes activeBg: Int,
    @DrawableRes normalBg: Int,
    onFocus: ((View, Boolean) -> Unit)? = null
) {
    this.onFocusChangeListener = View.OnFocusChangeListener { view, isFocus ->
        if (isFocus) {
            this.setBackgroundResource(activeBg)
        } else {
            this.setBackgroundResource(normalBg)
        }
        onFocus?.invoke(view, isFocus)
    }
}