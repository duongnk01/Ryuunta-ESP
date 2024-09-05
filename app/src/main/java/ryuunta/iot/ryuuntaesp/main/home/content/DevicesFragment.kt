package ryuunta.iot.ryuuntaesp.main.home.content

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import ryuunta.iot.ryuuntaesp.MainViewModel
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.core.base.BaseFragment
import ryuunta.iot.ryuuntaesp.data.model.DeviceObj
import ryuunta.iot.ryuuntaesp.data.model.ElementInfoObj
import ryuunta.iot.ryuuntaesp.databinding.FragmentDevicesBinding
import ryuunta.iot.ryuuntaesp.dialog.DialogChangeInfoDevice
import ryuunta.iot.ryuuntaesp.dialog.DialogUpdateNameDevice
import ryuunta.iot.ryuuntaesp.helper.ControlHelper
import ryuunta.iot.ryuuntaesp.helper.DeviceHelper
import ryuunta.iot.ryuuntaesp.utils.RLog
import ryuunta.iot.ryuuntaesp.utils.developInProgress
import ryuunta.iot.ryuuntaesp.utils.hideKeyboard
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick
import ryuunta.iot.ryuuntaesp.utils.showDialogError
import ryuunta.iot.ryuuntaesp.utils.showDialogNegative
import ryuunta.iot.ryuuntaesp.utils.showDialogNotification
import ryuunta.iot.ryuuntaesp.utils.showDialogNotificationAutoDismiss

class DevicesFragment : BaseFragment<FragmentDevicesBinding, MainViewModel>(
    FragmentDevicesBinding::inflate,
    MainViewModel::class.java
) {
    private val TAG = "DevicesFragment"

    private val args: DevicesFragmentArgs by navArgs()

    private val deviceHelper = DeviceHelper()
    private val controlHelper = ControlHelper()

    private var device: DeviceObj? = null

    private val dialogChangeInfoDevice: DialogChangeInfoDevice by lazy {
        DialogChangeInfoDevice(requireContext()) {
            requireContext().showDialogNotificationAutoDismiss(
                R.string.txt_done,
                R.raw.anim_paimon_like,
                lifecycle,
                R.string.txt_device_info_updated
            )
        }
    }

    private val dialogUpdateNameDevice: DialogUpdateNameDevice by lazy {
        DialogUpdateNameDevice(requireContext()) {
            requireContext().showDialogNotification(
                R.string.txt_done,
                R.raw.anim_paimon_like,
                lifecycle,
                R.string.txt_new_device_name_updated
            )
        }
    }

    private val onError: (Int, String) -> Unit = { code, message ->
        RLog.e(TAG, "onError: $message -- code $code")
//        try {
//            requireContext().showDialogError(lifecycle, message) {
//                if (code == -1) {
//                    navController.popBackStack()
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

    }

    override fun initViews(savedInstanceState: Bundle?) {

        binding.apply {
            val deviceId = args.deviceId
            deviceHelper.getDeviceById(deviceId) {
                device = it
                device?.let { deviceItem ->

                    layoutElementButton.initView(deviceItem, onElementClick = { elm, state ->
                        RLog.d(
                            TAG,
                            "initView: onElementClick ${elm.values.first().label} -- state is ${elm.values.first().value}"
                        )
                        controlStateElement(elm, state)
                    },
                        onElmLongClick = { elmId ->
                            dialogUpdateNameDevice.show(lifecycle, deviceId, elmId)
                        })
                    txtDeviceLabel.text = deviceItem.label

                    if (deviceItem.roomId == null) {
                        txtRoomLabel.text = getString(R.string.txt_still_no_room)
                    } else {
                        viewModel.getRoomName(deviceItem.roomId!!) { deviceName ->
                            txtRoomLabel.text = deviceName
                        }
                    }
                    //when view created -> get state of device elm from firebase and update UI
                    controlStateElement(deviceItem.buttonList)
                }
            }
            viewModel.getDeviceName(deviceId) {
                txtDeviceLabel.text = it
            }

        }
    }

    override fun initEvents() {
        super.initEvents()
        binding.apply {
            container.setPreventDoubleClick {
                it?.hideKeyboard()
            }
            btnNavBack.setPreventDoubleClick {
                navController.popBackStack()
            }

            btnTurnOnAll.setPreventDoubleClick {
                turnAll(true)
            }
            btnTurnOffAll.setPreventDoubleClick {
                turnAll(false)
            }

            btnEditDevice.setPreventDoubleClick {
                showPopUpMenu(it!!)
            }
        }
    }

    private fun turnAll(isOn: Boolean) {
        device?.let {
            controlStateElement(it.buttonList, isOn)
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

    private fun showPopUpMenu(view: View) {
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.menu_edit_device, menu)
            val itemDelete = menu.getItem(menu.size() - 1)
            itemDelete.actionView?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.red_error
                )
            )
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.btn_edit -> {
                        device?.let { dev ->
                            dialogChangeInfoDevice.show(lifecycle, dev.id, dev.label, dev.roomId)
                        }
                    }

                    R.id.btn_delete_device -> {
                        device?.let { dev ->
                            requireContext().showDialogNegative(
                                R.string.txt_delete_device,
                                R.string.txt_are_you_sure_delete_device,
                                lottieAnim = R.raw.anim_nahida_question,
                                lifecycle = lifecycle,
                                cancelRes = R.string.delete,
                                confirmRes = R.string.cancel,
                                onCancel = {
                                    deviceHelper.deleteDevice(dev.id) {
                                        requireContext().showDialogNotificationAutoDismiss(
                                            R.string.txt_done,
                                            R.raw.anim_paimon_like,
                                            lifecycle,
                                            R.string.txt_device_deleted,
                                            timeDismiss = 1000
                                        ) {
                                            navController.popBackStack()

                                        }
                                    }
                                }
                            )
                        }


                    }

                    else -> {
                        device?.let { dev ->
                            requireContext().developInProgress(lifecycle)
                        }
                    }
                }
                true
            }
            show()
        }
    }


    override fun isCustomBackPress(): Boolean {
        return true
    }

    override fun customBackPress() {
        super.customBackPress()
        navController.popBackStack()
    }
}