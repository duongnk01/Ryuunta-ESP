package ryuunta.iot.ryuuntaesp.common

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.content.Context
import androidx.annotation.RawRes
import androidx.lifecycle.Lifecycle
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.databinding.DialogLottieBinding

class DialogLottie(context: Context) : RyuuntaDialog<DialogLottieBinding>(context) {
    override fun onDialogShown() {
        binding {
//            animationView.setAnimation(R.raw.anim_ghibli_witch_on_broom)
//            animationView.playAnimation()
//            animationView.repeatCount = -1
        }
    }

    fun show(
        lifecycle: Lifecycle,
        @RawRes lottieAnim: Int,
        isLoop: Boolean = false
    ) {
        binding {
            animationView.setAnimation(lottieAnim)
            animationView.repeatCount = if (isLoop) -1 else 0
        }
        super.show(lifecycle)
    }

    fun onDismissWhenAnimationDone(onComplete: () -> Unit = {}) {
        binding {
            animationView.addAnimatorListener(object : AnimatorListener {
                override fun onAnimationStart(p0: Animator) {

                }

                override fun onAnimationEnd(p0: Animator) {
                    onDismissAndRemoveRes()
                    onComplete()
                }

                override fun onAnimationCancel(p0: Animator) {

                }

                override fun onAnimationRepeat(p0: Animator) {

                }

            })
        }
    }

    override fun isCancellable(): Boolean = false

    override fun onCreate() {

    }

    override fun getLayoutId(): Int = R.layout.dialog_lottie
    override fun initBinding(): DialogLottieBinding {
        return DialogLottieBinding.bind(contentView)
    }

}