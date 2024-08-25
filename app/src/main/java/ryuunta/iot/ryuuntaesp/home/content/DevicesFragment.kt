package ryuunta.iot.ryuuntaesp.home.content

import android.os.Bundle
import androidx.navigation.fragment.navArgs
import com.ryuunta.iot.esp.widget.hideSoftKeyboard
import ryuunta.iot.ryuuntaesp.core.base.BaseFragment
import ryuunta.iot.ryuuntaesp.core.helper.ControlHelper
import ryuunta.iot.ryuuntaesp.core.helper.DeviceHelper
import ryuunta.iot.ryuuntaesp.data.model.DeviceObj
import ryuunta.iot.ryuuntaesp.databinding.FragmentDevicesBinding
import ryuunta.iot.ryuuntaesp.utils.RLog
import ryuunta.iot.ryuuntaesp.utils.hideKeyboard
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick

class DevicesFragment : BaseFragment<FragmentDevicesBinding, DevicesViewModel>(
    FragmentDevicesBinding::inflate,
    DevicesViewModel::class.java
) {
    private val TAG = "DevicesFragment"

    private val args: DevicesFragmentArgs by navArgs()

    private val deviceHelper = DeviceHelper()
    private val controlHelper = ControlHelper()

    private var device: DeviceObj? = null

    private val elementsAdapter: DeviceElementsAdapter by lazy {
        DeviceElementsAdapter {}
    }

    override fun initViews(savedInstanceState: Bundle?) {

        binding.apply {
            val deviceId = args.deviceId
            device = deviceHelper.getDeviceById(deviceId)
            device?.let { deviceItem ->
                //when view created -> get state of device elm from firebase and update UI
                controlStateElement(deviceItem.buttonList.map { it.value })

                layoutElementButton.initView(deviceItem) { elmPath, state ->
                    RLog.d(TAG, "initView: onElementClick $elmPath -- state is $state")
                    controlStateElement(listOf(elmPath), state)
                }
                txtDeviceLabel.text = deviceItem.name
            }

//            rcvDeviceElements.adapter = elementsAdapter
//            val fanRemote = DeviceHelper().getAllDevices()[0]
//            val listElms = fanRemote.buttonList.map {
//                DeviceElement(fanRemote.devPath, it.value)
//            }.toList()
//            elementsAdapter.submitList(listElms)

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

    private fun controlStateElement(elmsPath: List<String>, state: Boolean? = null) {
        RLog.d(TAG, "controlStateElement: $elmsPath")
        controlHelper.controlDevice(device?.devPath!!, elmsPath, state) { elmPath, isOn ->
            RLog.d(TAG, "controlStateElement: control from ${if (state == null) "server" else "app"}")
            binding.layoutElementButton.updateView(elmPath, isOn)
        }
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