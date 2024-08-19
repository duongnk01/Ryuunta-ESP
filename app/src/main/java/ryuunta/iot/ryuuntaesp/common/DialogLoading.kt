package ryuunta.iot.ryuuntaesp.common

import android.content.Context
import androidx.lifecycle.Lifecycle
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.databinding.DialogLoadingBinding

class DialogLoading(context: Context) : RyuuntaDialog<DialogLoadingBinding>(context) {
    override fun onDialogShown() {
        binding {
//            animationView.setAnimation(R.raw.anim_ghibli_witch_on_broom)
//            animationView.playAnimation()
//            animationView.repeatCount = -1
        }
    }

    fun show(lifecycle: Lifecycle, titleRes: Int, subTitleRes: Int) {
        binding {
            txtMessage.setText(subTitleRes)
            txtTitle.setText(titleRes)
        }
        super.show(lifecycle)
    }

    fun show(lifecycle: Lifecycle, titleRes: Int) {
        binding {
            txtMessage.text = ""
            txtTitle.setText(titleRes)
        }
        super.show(lifecycle)
    }

    fun show(lifecycle: Lifecycle, message: String) {
        binding {
            txtMessage.text = message
            txtTitle.setText(R.string.wait_a_few)
        }
        super.show(lifecycle)
    }

    fun show(lifecycle: Lifecycle, titleRes: Int, subTitleRes: String) {
        binding {
            txtMessage.text = subTitleRes
            txtTitle.setText(titleRes)
        }
        super.show(lifecycle)
    }

    override fun isCancellable(): Boolean = false

    override fun onCreate() {

    }

    override fun getLayoutId(): Int = R.layout.dialog_loading
    override fun initBinding(): DialogLoadingBinding {
        return DialogLoadingBinding.bind(contentView)
    }

}