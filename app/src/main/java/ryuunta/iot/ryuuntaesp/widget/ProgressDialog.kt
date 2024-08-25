package ryuunta.iot.ryuuntaesp.widget

import android.app.Dialog
import android.content.Context
import android.view.Window
import ryuunta.iot.ryuuntaesp.R

@Suppress("DEPRECATION")
class ProgressDialog(context: Context) : Dialog(context) {

    init {
        initView()
    }

    private fun initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_progress_loading)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        dismiss()
    }
}
