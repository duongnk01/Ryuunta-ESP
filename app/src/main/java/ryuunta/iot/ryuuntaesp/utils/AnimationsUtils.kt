package ryuunta.iot.ryuuntaesp.utils

import android.animation.Animator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator


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