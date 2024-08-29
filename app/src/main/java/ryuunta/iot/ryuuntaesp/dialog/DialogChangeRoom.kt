package ryuunta.iot.ryuuntaesp.dialog

import android.content.Context
import androidx.lifecycle.Lifecycle
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.common.RyuuntaDialog
import ryuunta.iot.ryuuntaesp.databinding.DialogChangeRoomBinding
import ryuunta.iot.ryuuntaesp.databinding.DialogUpdateNameDeviceBinding
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick

class DialogChangeRoom(context: Context) : RyuuntaDialog<DialogChangeRoomBinding>(context) {

    private var deviceId = ""

    override fun onDialogShown() {
        super.onDialogShown()

    }

    override fun onCreate() {
        binding.btnNavBack.setPreventDoubleClick {
            onDismissAndRemoveRes()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.dialog_change_room
    }

    fun show(lifecycle: Lifecycle, deviceId: String) {
        this.deviceId = deviceId
        show(lifecycle)
    }

    override fun initBinding(): DialogChangeRoomBinding = DialogChangeRoomBinding.bind(contentView)
}