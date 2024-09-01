package ryuunta.iot.ryuuntaesp.dialog

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.common.RyuuntaDialog
import ryuunta.iot.ryuuntaesp.databinding.DialogUpdateNameDeviceBinding
import ryuunta.iot.ryuuntaesp.helper.DeviceHelper
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick

class DialogUpdateNameDevice(context: Context, val onComplete: () -> Unit) : RyuuntaDialog<DialogUpdateNameDeviceBinding>(context) {

    private val deviceHelper = DeviceHelper()

    private var deviceId = ""
    private var elementId = ""

    override fun onDialogShown() {
        super.onDialogShown()
        deviceHelper.getDeviceById(deviceId) {
            if (elementId.isNotEmpty()) {
                binding.edtDevice.setText(it.buttonList[elementId]?.label)
                return@getDeviceById
            }
            binding.name = it.label
        }

    }

    override fun onCreate() {
        binding.btnNavBack.setPreventDoubleClick {
            onDismissAndRemoveRes()
        }

        binding.btnConfirm.setPreventDoubleClick {
            val newName = binding.edtDevice.text?.trim()
            if (!newName.isNullOrEmpty()) {
                if (elementId.isNotEmpty()) {
                    deviceHelper.changeNameElement(deviceId, elementId, newName.toString()) {
                        onDismissAndRemoveRes()
                        onComplete()
                    }
                    return@setPreventDoubleClick
                }
                deviceHelper.changeNameDevice(deviceId, newName.toString()) {
                    onDismissAndRemoveRes()
                    onComplete()
                }
            } else {
                Toast.makeText(context, context.getString(R.string.name_cannot_be_empty), Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun show(lifecycle: Lifecycle, deviceId: String, elementId: String = "") {
        this.deviceId = deviceId
        this.elementId = elementId
        show(lifecycle)
    }

    override fun getLayoutId(): Int {
        return R.layout.dialog_update_name_device
    }

    override fun initBinding(): DialogUpdateNameDeviceBinding = DialogUpdateNameDeviceBinding.bind(contentView)
}