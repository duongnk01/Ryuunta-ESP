package ryuunta.iot.ryuuntaesp.utils

import android.animation.Animator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.Transformation
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout

fun slideInBottom(view: View) {
    val translateAnim = TranslateAnimation(
        Animation.RELATIVE_TO_SELF,
        0f,
        Animation.RELATIVE_TO_SELF,
        0f,
        Animation.RELATIVE_TO_SELF,
        1f,
        Animation.RELATIVE_TO_SELF,
        0f
    )
    translateAnim.duration = 280
    translateAnim.fillAfter = true
    view.startAnimation(translateAnim)
}

fun slideCentreUp(view: View, duration: Long = 280, onStart: () -> Unit = {}, onEnd: () -> Unit = {}) {
    val translateAnim = TranslateAnimation(
        Animation.RELATIVE_TO_PARENT,
        0f,
        Animation.RELATIVE_TO_PARENT,
        0f,
        Animation.RELATIVE_TO_PARENT,
        1f,
        Animation.RELATIVE_TO_PARENT,
        0f
    )
    translateAnim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation?) {
            onStart()
        }

        override fun onAnimationEnd(p0: Animation?) {
            onEnd()
        }

        override fun onAnimationRepeat(p0: Animation?) {

        }

    })
    translateAnim.duration = duration
    translateAnim.fillAfter = true
    view.startAnimation(translateAnim)
}

fun fadeIn(view: View, duration: Long = 1000, onStart: () -> Unit = {}, onEnd: () -> Unit = {}) {
    view.visibility = View.VISIBLE

    val fadeInAnim = AlphaAnimation(0f, 1f)
    fadeInAnim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation?) {
            onStart()
        }

        override fun onAnimationEnd(p0: Animation?) {
            onEnd()
        }

        override fun onAnimationRepeat(p0: Animation?) {

        }


    })
    fadeInAnim.interpolator = DecelerateInterpolator() //add this
    fadeInAnim.fillAfter = true
    fadeInAnim.duration = duration

    view.startAnimation(fadeInAnim)
}

fun fadeOut(view: View, duration: Long = 1000, onStart: () -> Unit = {}, onEnd: () -> Unit = {}) {
    val fadeOutAnim = AlphaAnimation(1f, 0f)
    fadeOutAnim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation?) {
            onStart()
        }

        override fun onAnimationEnd(p0: Animation?) {
            onEnd()
        }

        override fun onAnimationRepeat(p0: Animation?) {

        }

    })
    fadeOutAnim.interpolator = AccelerateInterpolator() //and this
    fadeOutAnim.startOffset = 1000
    fadeOutAnim.fillAfter = false
    fadeOutAnim.duration = duration
    view.startAnimation(fadeOutAnim)
    view.visibility = View.GONE

}

// slide the view from below itself to the current position
fun slideLeftToShow(view: View, onStart: () -> Unit, onEnd: () -> Unit) {
    val animate = TranslateAnimation(
        view.width.toFloat(),  // fromXDelta
        0f,  // toXDelta
        0f,  // fromYDelta
        0f
    ) // toYDelta
    animate.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {
            onStart()
        }

        override fun onAnimationEnd(animation: Animation?) {
            onEnd()
        }

        override fun onAnimationRepeat(animation: Animation?) {}
    })
    animate.duration = 600
    animate.fillAfter = false
    view.startAnimation(animate)
}

// slide the view from its current position to below itself
fun slideLeftToHide(view: View, onStart: () -> Unit, onEnd: () -> Unit) {
    val animate = TranslateAnimation(
        0f,  // fromXDelta
        -view.width.toFloat(),  // toXDelta
        0f,  // fromYDelta
        0f
    ) // toYDelta
    animate.duration = 600
    animate.fillAfter = false
    animate.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) = onStart()
        override fun onAnimationEnd(animation: Animation?) = onEnd()
        override fun onAnimationRepeat(animation: Animation?) {}
    })
    view.startAnimation(animate)
}

// slide the view from below itself to the current position
fun slideRightToShow(view: View, onStart: () -> Unit, onEnd: () -> Unit) {
    val animate = TranslateAnimation(
        -view.width.toFloat(),  // fromXDelta
        0f,  // toXDelta
        0f,  // fromYDelta
        0f
    ) // toYDelta
    animate.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {
            onStart()
        }

        override fun onAnimationEnd(animation: Animation?) {
            onEnd()
        }

        override fun onAnimationRepeat(animation: Animation?) {}
    })
    animate.duration = 600
    animate.fillAfter = false
    view.startAnimation(animate)
}

// slide the view from its current position to below itself
fun slideRightToHide(view: View, onStart: () -> Unit, onEnd: () -> Unit) {
    val animate = TranslateAnimation(
        0f,  // fromXDelta
        view.width.toFloat(),  // toXDelta
        0f,  // fromYDelta
        0f
    ) // toYDelta
    animate.duration = 600
    animate.fillAfter = false
    animate.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {
            onStart()
        }

        override fun onAnimationEnd(animation: Animation?) {
            onEnd()
        }

        override fun onAnimationRepeat(animation: Animation?) {}
    })
    view.startAnimation(animate)
}

fun slideToNextView(startView: View, endView: View) {
    slideLeftToHide(startView, {
        endView.visibility = View.VISIBLE
        slideLeftToShow(endView, {}, {})
    }, { startView.visibility = View.GONE })
}

fun slideToPrevView(startView: View, endView: View) {
    slideRightToHide(startView, {
        endView.visibility = View.VISIBLE
        slideRightToShow(endView, {}, {})
    }) { startView.visibility = View.GONE }
}

fun expand(v: View) {
    val matchParentMeasureSpec =
        View.MeasureSpec.makeMeasureSpec((v.parent as View).width, View.MeasureSpec.EXACTLY)
    val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    v.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    val targetHeight = v.measuredHeight

    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    v.layoutParams.height = 1
    v.visibility = View.VISIBLE
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            v.layoutParams.height =
                if (interpolatedTime == 1f) LinearLayout.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
            v.requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // Expansion speed of 1dp/ms
    a.duration = (targetHeight / v.context.resources.displayMetrics.density).toInt().toLong()
    v.startAnimation(a)
}

fun collapse(v: View) {
    val initialHeight = v.measuredHeight
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime == 1f) {
                v.visibility = View.GONE
            } else {
                v.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                v.requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // Collapse speed of 1dp/ms
    a.duration = (initialHeight / v.context.resources.displayMetrics.density).toInt().toLong()
    v.startAnimation(a)
}