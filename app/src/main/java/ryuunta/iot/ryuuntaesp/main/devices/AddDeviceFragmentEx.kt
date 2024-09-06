package ryuunta.iot.ryuuntaesp.main.devices

import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.provider.Settings
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.core.base.Config
import ryuunta.iot.ryuuntaesp.data.model.ElementInfoObj
import ryuunta.iot.ryuuntaesp.main.home.devices.DeviceViewType
import ryuunta.iot.ryuuntaesp.utils.RLog
import ryuunta.iot.ryuuntaesp.utils.connectToWifi
import ryuunta.iot.ryuuntaesp.utils.developInProgress
import ryuunta.iot.ryuuntaesp.utils.randomId
import ryuunta.iot.ryuuntaesp.utils.sendDataToESP8266
import ryuunta.iot.ryuuntaesp.utils.showDialogError
import ryuunta.iot.ryuuntaesp.utils.showDialogNegative

fun AddDeviceFragment.generateDeviceObj(devType: Int) {
    val listButton = mutableMapOf<String, ElementInfoObj>()
    when (devType) {
        DeviceViewType.SWITCH_BUTTON.code -> {
            listButton["1_btn"] = ElementInfoObj("1_btn", "Button 1", 0)
        }

        DeviceViewType.SWITCH_4_BUTTON.code -> {
            listButton["1_btn"] = ElementInfoObj("1_btn", "Button 1", 0)
            listButton["2_btn"] = ElementInfoObj("2_btn", "Button 2", 0)
            listButton["3_btn"] = ElementInfoObj("3_btn", "Button 3", 0)
            listButton["4_btn"] = ElementInfoObj("4_btn", "Button 4", 0)
        }

//        DeviceViewType.FAN_REMOTE.code, DeviceViewType.DOOR_LOCK.code -> {
//            requireContext().developInProgress(lifecycle)
//            return
//        }
    }

    deviceObj.id = randomId()
    deviceObj.label = DeviceViewType.entries.find { it.code == devType }?.name.toString()
    deviceObj.type = DeviceViewType.entries.find { it.code == devType }?.name
        ?: DeviceViewType.SWITCH_BUTTON.name

    deviceObj.buttonList = listButton
}

fun AddDeviceFragment.confirmCancel() {
    requireContext().showDialogNegative(
        R.string.txt_cancel_add_device,
        R.string.txt_you_dont_to_want_add_device_anymore,
        lifecycle = lifecycle,
        lottieAnim = R.raw.anim_nahida_question,
        cancelRes = R.string.txt_no,
        isAnimLoop = true,
        confirmRes = R.string.txt_next,
        onCancel = {
//            timeUtils.onCancel()
            if (deviceObj.id.isNotEmpty()) {
                deviceHelper.deleteDevice(deviceObj.id) {
                    clearCacheWhenExit()

                }
            } else {
                clearCacheWhenExit()
            }
        }
    )
}

fun AddDeviceFragment.startGenerateDevice() {
    binding.txtDescSetup.text = getString(R.string.txt_generate_data_device)

    val espSSID = when (devType) {
        DeviceViewType.SWITCH_BUTTON.code -> "RYUUNTA_ESP_01_AP"

        DeviceViewType.SWITCH_4_BUTTON.code -> "RYUUNTA_ESP_D1_AP"
        else -> ""
    }

    deviceHelper.addNewDevice(deviceObj, onCompleted = { devId ->
        binding.txtDescSetup.text = "Kết nối tới $espSSID"
        changeWifiESPForResult.launch(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))



    }, onFailure = {
        requireContext().showDialogError(
            lifecycle,
            getString(R.string.txt_cannot_generate_device_try_later)
        ) {
            clearCacheWhenExit()
        }
    })

//    timeUtils.startTimer(60, onRun = {
//        RLog.d("TIMER", "time = $it")
//    }, onFinish = {
//        requireActivity().runOnUiThread {
//            requireContext().showDialogError(
//                lifecycle,
//                getString(R.string.txt_cannot_generate_device_try_later)
//            ) {
//                if (deviceObj.id.isNotEmpty()) {
//                    deviceHelper.deleteDevice(deviceObj.id) {
//                        clearCacheWhenExit()
//
//                    }
//                } else {
//                    clearCacheWhenExit()
//                }
//            }
//        }
//
//    })
}



fun AddDeviceFragment.connectESPWifi() {

    val ssid = binding.edtInputSsid.text.toString()
    val pwd = binding.edtInputPass.text.toString()

    val dataToSend =
        "$ssid:$pwd:${Config.userUid}/houses/${Config.currentHouseId}/devices/${deviceObj.id}:"
    val espIp2 = Config.softAPIPAddress
    val port = Config.espPort

    binding.txtDescSetup.text = getString(R.string.txt_config_esp_network)
    sendDataToESP8266(dataToSend, espIp2, port, { isSuccess ->
        //on received boolean
        if (isSuccess) {

            //connect thành công
            binding.txtDescSetup.text = getString(R.string.txt_esp_already_connected)
            Thread.sleep(1000)
            binding.txtDescSetup.text = getString(R.string.txt_esp_config_device)
            //show dialog set name, select group
            dialogSetInfoDevice.show(
                lifecycle,
                deviceObj.id,
                DeviceViewType.entries.find { it.name == deviceObj.type }?.code ?: -1
            )

        } else {
            //connect thất bại
            binding.txtDescSetup.text = getString(R.string.txt_esp_not_connected)
            if (deviceObj.id.isNotEmpty()) {
                deviceHelper.deleteDevice(deviceObj.id) {
                    stepView.prevStep()

                }
            }

        }

    }, onError = { errorMess ->
        requireContext().showDialogError(
            lifecycle,
            errorMess
        ) {
            clearCacheWhenExit()
        }

    })

}

interface GenerateDeviceCallback {
    fun onTimeRemaining(time: Int)
    fun onSetupGuideDescription(resGuideDesc: Int)
}