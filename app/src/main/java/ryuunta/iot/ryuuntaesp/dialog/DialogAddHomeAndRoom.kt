package ryuunta.iot.ryuuntaesp.dialog

import android.content.Context
import androidx.lifecycle.Lifecycle
import ryuunta.iot.ryuuntaesp.common.RyuuntaDialog
import ryuunta.iot.ryuuntaesp.databinding.DialogHomeAndRoomBinding

class DialogAddHomeAndRoom(
    context: Context
) : RyuuntaDialog<DialogHomeAndRoomBinding>(context) {

    override fun onDialogShown() {
        super.onDialogShown()
    }
    override fun onCreate() {
//        TODO("Not yet implemented")
    }

    override fun initBinding(): DialogHomeAndRoomBinding {
        return DialogHomeAndRoomBinding.bind(contentView)
    }


}