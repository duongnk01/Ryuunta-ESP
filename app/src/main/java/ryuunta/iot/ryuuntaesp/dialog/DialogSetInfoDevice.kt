package ryuunta.iot.ryuuntaesp.dialog

import android.content.Context
import androidx.lifecycle.Lifecycle
import com.ryuunta.iot.esp.widget.hideSoftKeyboard
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.adapter.RoomSelectionAdapter
import ryuunta.iot.ryuuntaesp.common.RyuuntaDialog
import ryuunta.iot.ryuuntaesp.data.model.HouseObj
import ryuunta.iot.ryuuntaesp.databinding.DialogSetInfoDeviceBinding
import ryuunta.iot.ryuuntaesp.helper.DeviceHelper
import ryuunta.iot.ryuuntaesp.helper.GroupHelper
import ryuunta.iot.ryuuntaesp.main.devices.ResourceFactory.getNameByViewType
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick

class DialogSetInfoDevice(
    context: Context,
    private val onDone: () -> Unit
) : RyuuntaDialog<DialogSetInfoDeviceBinding>(context) {

    private var deviceId = ""
    private var deviceName = ""
    private var deviceRoomId: String? = null
    private var devType = -1

    private val deviceHelper = DeviceHelper()
    private val groupHelper = GroupHelper()

    private var currentRoomSelected: HouseObj.RoomObj? = null

    private val roomSelectionAdapter: RoomSelectionAdapter by lazy {
        RoomSelectionAdapter { item ->
                currentRoomSelected = item

        }
    }

    override fun onDialogShown() {
        super.onDialogShown()
        binding.edtName.hint = deviceName.ifEmpty { context.getNameByViewType(devType) }
        binding.rcvRoom.adapter = roomSelectionAdapter
        groupHelper.getRoomsInHouse { rooms ->
            if (deviceRoomId != null) {
                roomSelectionAdapter.selectedPosition = rooms.indexOfFirst { it.id == deviceRoomId }
            }
            roomSelectionAdapter.submitList(rooms)

        }
    }

    override fun onCreate() {
        binding.container.setPreventDoubleClick {
            hideSoftKeyboard(it)
        }
        binding.btnConfirm.setPreventDoubleClick {
            hideSoftKeyboard(it)
            val name =
                binding.edtName.text.toString().trim().ifEmpty { binding.edtName.hint.toString().trim() }
            deviceHelper.changeNameDevice(deviceId, name) {
                if (currentRoomSelected != null) {
                    groupHelper.changeRoom(deviceId, currentRoomSelected!!.id) {
                        onDone()
                        onDismissAndRemoveRes()
                    }
                } else {
                    onDone()
                    onDismissAndRemoveRes()
                }
            }

        }
    }

    fun show(lifecycle: Lifecycle, deviceId: String, devType: Int) {
        this.deviceId = deviceId
        this.devType = devType
        show(lifecycle)
    }


    override fun isCancellable(): Boolean {
        return false
    }

    override fun getLayoutId(): Int = R.layout.dialog_set_info_device

    override fun initBinding(): DialogSetInfoDeviceBinding =
        DialogSetInfoDeviceBinding.bind(contentView)
}