package ryuunta.iot.ryuuntaesp.main.home.content

import android.os.Bundle
import androidx.navigation.fragment.navArgs
import ryuunta.iot.ryuuntaesp.core.base.BaseFragment
import ryuunta.iot.ryuuntaesp.data.model.DeviceObj
import ryuunta.iot.ryuuntaesp.data.model.ElementInfoObj
import ryuunta.iot.ryuuntaesp.databinding.FragmentDevicesBinding
import ryuunta.iot.ryuuntaesp.helper.ControlHelper
import ryuunta.iot.ryuuntaesp.helper.DeviceHelper
import ryuunta.iot.ryuuntaesp.utils.RLog
import ryuunta.iot.ryuuntaesp.utils.hideKeyboard
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick
import ryuunta.iot.ryuuntaesp.utils.showDialogError

class DevicesFragment : BaseFragment<FragmentDevicesBinding, DevicesViewModel>(
    FragmentDevicesBinding::inflate,
    DevicesViewModel::class.java
) {
    private val TAG = "DevicesFragment"

    private val args: DevicesFragmentArgs by navArgs()

    private val deviceHelper = DeviceHelper()
    private val controlHelper = ControlHelper()

    private var device: DeviceObj? = null

    private val onError: (Int, String) -> Unit = { code, message ->
        RLog.e(TAG, "onError: $message")
        requireContext().showDialogError(lifecycle, message)
    }


    override fun initViews(savedInstanceState: Bundle?) {

        binding.apply {
            val deviceId = args.deviceId
            deviceHelper.getDeviceById(deviceId) {
                device = it
                device?.let { deviceItem ->
                    //when view created -> get state of device elm from firebase and update UI
                    controlStateElement(deviceItem.buttonList)

                    layoutElementButton.initView(deviceItem) { elm, state ->
                        RLog.d(TAG, "initView: onElementClick ${elm.label} -- state is ${elm.value}")
                        controlStateElement(mapOf(elm.id to elm), state)
                    }
                    txtDeviceLabel.text = deviceItem.label
                }
            }


        }
    }

    override fun initEvents() {
        super.initEvents()
        binding.apply {
            container.setPreventDoubleClick {
                it?.hideKeyboard()
            }
            txtDeviceLabel.setPreventDoubleClick {
                navController.popBackStack()
            }
        }
    }

    private fun controlStateElement(elmsPath: Map<String, ElementInfoObj>, state: Boolean? = null) {
        RLog.d(TAG, "controlStateElement: $elmsPath")
        controlHelper.controlDevice(
            device?.id!!,
            elmsPath,
            state,
            onStateUpdated = { elm ->
                RLog.d(
                    TAG,
                    "controlStateElement: control from ${if (state == null) "server" else "app"}"
                )
                binding.layoutElementButton.updateView(elm)
            },
            onError = onError
        )
//        if (state != null) { //control from app
//
//        } else {
//            controlHelper.controlDevice(device?.devPath!!, elmsPath) { elmPath, isOn ->
//                RLog.d(TAG, "controlStateElement: control from server")
//                binding.layoutElementButton.updateView(elmPath, isOn)
//            }
//        }


    }


    override fun isCustomBackPress(): Boolean {
        return true
    }

    override fun customBackPress() {
        super.customBackPress()
        navController.popBackStack()
    }
}