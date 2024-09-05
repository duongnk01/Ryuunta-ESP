package ryuunta.iot.ryuuntaesp.main.devices

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.RMainActivity
import ryuunta.iot.ryuuntaesp.core.base.BaseFragment
import ryuunta.iot.ryuuntaesp.data.model.DeviceObj
import ryuunta.iot.ryuuntaesp.databinding.FragmentAddDeviceBinding
import ryuunta.iot.ryuuntaesp.dialog.DialogSetInfoDevice
import ryuunta.iot.ryuuntaesp.dialog.DialogWifiSelection
import ryuunta.iot.ryuuntaesp.helper.DeviceHelper
import ryuunta.iot.ryuuntaesp.main.devices.ResourceFactory.getListDeviceType
import ryuunta.iot.ryuuntaesp.utils.PermissionUtils.checkPermissionsNew
import ryuunta.iot.ryuuntaesp.utils.PermissionUtils.permissionsForScanWifi
import ryuunta.iot.ryuuntaesp.utils.RLog
import ryuunta.iot.ryuuntaesp.utils.TimeUtils
import ryuunta.iot.ryuuntaesp.utils.getCurrentWifiConnection
import ryuunta.iot.ryuuntaesp.utils.hideKeyboard
import ryuunta.iot.ryuuntaesp.utils.scanWifi
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick
import ryuunta.iot.ryuuntaesp.utils.showDialogResultAnnounce
import ryuunta.iot.ryuuntaesp.widget.StepView

class AddDeviceFragment : BaseFragment<FragmentAddDeviceBinding, AddDeviceViewModel>(
    FragmentAddDeviceBinding::inflate,
    AddDeviceViewModel::class.java
) {
    private val TAG = "AddDeviceActivity"

    internal var devType = -1
    internal val deviceHelper = DeviceHelper()
    internal var deviceObj: DeviceObj = DeviceObj()
//    internal val timeUtils = TimeUtils()

    internal val stepView: StepView by lazy {
        StepView(requireContext())
    }

    private val dialogWifiSelection by lazy {
        DialogWifiSelection(requireContext()) {
            binding.edtInputSsid.text = it.ssid
        }
    }

    internal val dialogSetInfoDevice: DialogSetInfoDevice by lazy {
        DialogSetInfoDevice(requireContext()) {
            requireContext().showDialogResultAnnounce(
                R.string.txt_done,
                R.raw.anim_paimon_like,
                lifecycle,
                R.string.add_device_complete
            ) {
                clearCacheWhenExit()
            }
        }
    }

    private val selectDeviceTypeAdapter: SelectDeviceTypeAdapter by lazy {
        SelectDeviceTypeAdapter {
            devType = it.deviceType
            generateDeviceObj(it.deviceType)
            stepView.nextStep()
        }
    }

    internal val changeWifiESPForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (getCurrentWifiConnection(requireContext()).contains("RYUUNTA_ESP")) {
               connectESPWifi()

            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.txt_esp_not_connected), Toast.LENGTH_SHORT
                ).show()
            }

        }


    override fun initViews(savedInstanceState: Bundle?) {
        stepView.setStep(
            StepView.Step(binding.layoutSelectDevice),
//            StepView.Step(binding.layoutConnectEsp),
            StepView.Step(binding.layoutConfigEsp),
            StepView.Step(binding.layoutGenerateDevice)
        )


    }

    override fun onSetViewInfo() {
        super.onSetViewInfo()
        (activity as RMainActivity).requestBluetooth()
        checkPermissionsNew(
            requireContext(),
            permissionsForScanWifi, {

                binding.rcvDeviceType.adapter = selectDeviceTypeAdapter
                selectDeviceTypeAdapter.submitList(getListDeviceType())
            },
            onCancel = { deniedPermissions ->
                deniedPermissions.forEach {
                    RLog.d(TAG, it.permissionName.toString())

                }

            })

    }

    override fun initEvents() {
        super.initEvents()

        binding.btnNavBack.setPreventDoubleClick {
            customBackPress()
        }
        binding.btnGoToSetting.setPreventDoubleClick {
            val intentSetting = Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
            changeWifiESPForResult.launch(intentSetting)
        }
//        binding.edtInputSsid.text = getCurrentWifiConnection(requireContext()).ifEmpty { getString(R.string.txt_select_ssid) }
        binding.edtInputSsid.text = "Duongnk"
        binding.edtInputPass.setText("10101010")
        binding.edtInputSsid.setPreventDoubleClick {
            dialogWifiSelection.show(lifecycle)
        }
//        binding.swiperRefresh.setOnRefreshListener {
//            binding.swiperRefresh.isRefreshing = true
//            listSSID = scanWifi(requireContext())
//            ssidAdapter.submitList(listSSID)
//            ssidAdapter.notifyDataSetChanged()
//            binding.swiperRefresh.isRefreshing = false
//
//        }

        binding.btnAuthorize.setOnClickListener {
            requireActivity().hideKeyboard()

            //next step to countdown screen
            stepView.nextStep()
            startGenerateDevice()


        }

    }

    internal fun clearCacheWhenExit() {
        deviceObj = DeviceObj()
        devType = -1
//        timeUtils.onCancel()
        navController.popBackStack()

    }

    override fun isCustomBackPress(): Boolean {
        return true
    }

    override fun customBackPress() {
        super.customBackPress()
        if (stepView.currentStep != binding.layoutSelectDevice.id) {
            confirmCancel()

        } else {
            clearCacheWhenExit()
        }

    }
}