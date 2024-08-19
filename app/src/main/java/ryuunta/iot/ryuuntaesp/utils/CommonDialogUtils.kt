package ryuunta.iot.ryuuntaesp.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.databinding.DialogConfirmDeleteBinding
import ryuunta.iot.ryuuntaesp.databinding.DialogNotificationBinding
import ryuunta.iot.ryuuntaesp.databinding.DialogNotificationWithConfirmButtonBinding
import ryuunta.iot.ryuuntaesp.databinding.DialogResultAnnounceBinding


fun Context.showDialogNotificationWithConfirmButton(
    @StringRes titleRes: Int,
    @StringRes messRes: Int? = null,
    messStr: String,
    @RawRes lottieAnim: Int = R.raw.anim_nahida_question,
    lifecycle: Lifecycle,
    cancelRes: Int = R.string.cancel,
    confirmRes: Int = R.string.ok,
    isAnimLoop: Boolean = false,
    onCancel: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    var isShow = false
    val dialog: Dialog
    val view: View =
        LayoutInflater.from(this).inflate(R.layout.dialog_notification_with_confirm_button, null)
    val builder = AlertDialog.Builder(this)
        .setView(view)
        .setCancelable(false)


    dialog = builder.create()
    dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.getWindow()?.setGravity(Gravity.BOTTOM)

    val binding = DialogNotificationWithConfirmButtonBinding.bind(view)
    binding.txtTitle.setText(titleRes)

    if (messRes!=null)
        binding.txtMessage.setText(messRes)
    else
        binding.txtMessage.text = messStr

    binding.txtConfirm.setText(confirmRes)
    binding.txtCancel.setText(cancelRes)
    binding.animationView.setAnimation(lottieAnim)
    val repeatCount = if (isAnimLoop) -1 else 0
    if (repeatCount != binding.animationView.repeatCount) {
        binding.animationView.repeatCount = repeatCount
    }

    val observer = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_PAUSE -> {
                if (dialog.isShowing) {
                    isShow = true
                }
                dialog.dismiss()
            }

            Lifecycle.Event.ON_RESUME -> {
                if (isShow) {
                    dialog.show()
                }
            }
            else -> {

            }
        }
    }

    binding.txtConfirm.setPreventDoubleClick {
        onConfirm()
        isShow = false
        lifecycle.removeObserver(observer)
        dialog.dismiss()
    }

    binding.txtCancel.setPreventDoubleClick {
        onCancel()
        isShow = false
        lifecycle.removeObserver(observer)
        dialog.dismiss()
    }

    lifecycle.addObserver(observer)

    if (!dialog.isShowing) {
        dialog.show()
        isShow = true
    }

    if (!dialog.isShowing) {
        dialog.show()
        isShow = true
    }
}

/**
 * Dialog thông báo và trả kết quả (success or failure)
 * success: lottie = paimon_like
 * fail: lottie = nana_crying
 */
fun Context.showDialogNotification(
    @StringRes titleRes: Int,
    @RawRes lottieAnim: Int,
    lifecycle: Lifecycle,
    messRes: Int? = null,
    messStr: String = "",
    confirmRes: Int = R.string.ok,
    isAnimLoop: Boolean = false,
    onConfirm: () -> Unit = {},
) {
    var isShow = false
    val dialog: Dialog
    val view: View =
        LayoutInflater.from(this).inflate(R.layout.dialog_notification, null)
    val builder = AlertDialog.Builder(this)
        .setView(view)
        .setCancelable(false)


    dialog = builder.create()
    dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.getWindow()?.setGravity(Gravity.BOTTOM)

    val binding = DialogNotificationBinding.bind(view)
    binding.txtTitle.setText(titleRes)
    if (messRes == null) {
        binding.txtMessage.text = messStr
    } else {
        binding.txtMessage.setText(messRes)
    }

    binding.txtConfirm.setText(confirmRes)
    binding.animationView.setAnimation(lottieAnim)
    val repeatCount = if (isAnimLoop) -1 else 0
    if (repeatCount != binding.animationView.repeatCount) {
        binding.animationView.repeatCount = repeatCount
    }


    val observer = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_PAUSE -> {
                if (dialog.isShowing) {
                    isShow = true
                }
                dialog.dismiss()
            }

            Lifecycle.Event.ON_RESUME -> {
                if (isShow) {
                    dialog.show()
                }
            }
            else -> {

            }
        }
    }
    binding.txtConfirm.setPreventDoubleClick {
        onConfirm()
        isShow = false
        lifecycle.removeObserver(observer)
        dialog.dismiss()
    }
    lifecycle.addObserver(observer)

    if (!dialog.isShowing) {
        dialog.show()
        isShow = true
    }
}


fun Context.showDialogResultAnnounce(
    @StringRes titleRes: Int,
    @RawRes lottieAnim: Int = R.raw.anim_paimon_like,
    lifecycle: Lifecycle,
    messRes: Int? = null,
    messStr: String = "",
    isAnimLoop: Boolean = false,
    onConfirm: () -> Unit = {},
) {
    var isShow = false
    val dialog: Dialog
    val view: View =
        LayoutInflater.from(this).inflate(R.layout.dialog_result_announce, null)
    val builder = AlertDialog.Builder(this)
        .setView(view)
        .setCancelable(false)


    dialog = builder.create()
    dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.getWindow()?.setGravity(Gravity.BOTTOM)

    val binding = DialogResultAnnounceBinding.bind(view)
    binding.txtTitle.setText(titleRes)
    if (messRes == null) {
        binding.txtMessage.text = messStr
    } else {
        binding.txtMessage.setText(messRes)
    }

    binding.animationView.setAnimation(lottieAnim)
    val repeatCount = if (isAnimLoop) -1 else 0
    if (repeatCount != binding.animationView.repeatCount) {
        binding.animationView.repeatCount = repeatCount
    }


    val observer = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_PAUSE -> {
                if (dialog.isShowing) {
                    isShow = true
                }
                dialog.dismiss()
            }

            Lifecycle.Event.ON_RESUME -> {
                if (isShow) {
                    dialog.show()
                }
            }
            else -> {

            }
        }
    }
//    binding.btnConfirm.setPreventDoubleClick {
//        onConfirm()
//        isShow = false
//        lifecycle.removeObserver(observer)
//        dialog.dismiss()
//    }
    lifecycle.addObserver(observer)

    if (!dialog.isShowing) {
        dialog.show()
        isShow = true
    }
}

fun Context.showDialogNegative(
    @StringRes titleRes: Int,
    @StringRes messRes: Int? = null,
    messStr: String? = null,
    @RawRes lottieAnim: Int = R.raw.anim_paimon_bikkurisuru,
    lifecycle: Lifecycle,
    cancelRes: Int = R.string.cancel,
    confirmRes: Int = R.string.ok,
    isAnimLoop: Boolean = false,
    onCancel: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    var isShow = false
    val dialog: Dialog
    val view: View =
        LayoutInflater.from(this).inflate(R.layout.dialog_confirm_delete, null)
    val builder = AlertDialog.Builder(this)
        .setView(view)
        .setCancelable(false)


    dialog = builder.create()
    dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.getWindow()?.setGravity(Gravity.BOTTOM)

    val binding = DialogConfirmDeleteBinding.bind(view)
    binding.txtTitle.setText(titleRes)
    if (messRes == null) {
        binding.txtMessage.text = messStr
    } else {
        binding.txtMessage.setText(messRes)
    }

    binding.txtConfirm.setText(confirmRes)
    binding.txtCancel.setText(cancelRes)
    binding.animationView.setAnimation(lottieAnim)
    val repeatCount = if (isAnimLoop) -1 else 0
    if (repeatCount != binding.animationView.repeatCount) {
        binding.animationView.repeatCount = repeatCount
    }

    val observer = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_PAUSE -> {
                if (dialog.isShowing) {
                    isShow = true
                }
                dialog.dismiss()
            }

            Lifecycle.Event.ON_RESUME -> {
                if (isShow) {
                    dialog.show()
                }
            }
            else -> {

            }
        }
    }

    binding.txtConfirm.setPreventDoubleClick {
        onConfirm()
        isShow = false
        lifecycle.removeObserver(observer)
        dialog.dismiss()
    }

    binding.txtCancel.setPreventDoubleClick {
        onCancel()
        isShow = false
        lifecycle.removeObserver(observer)
        dialog.dismiss()
    }

    lifecycle.addObserver(observer)

    if (!dialog.isShowing) {
        dialog.show()
        isShow = true
    }

    if (!dialog.isShowing) {
        dialog.show()
        isShow = true
    }
}
