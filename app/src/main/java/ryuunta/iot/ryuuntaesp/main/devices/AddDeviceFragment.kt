package ryuunta.iot.ryuuntaesp.main.devices

import android.os.Bundle
import com.espressif.iot.esptouch.IEsptouchTask
import com.google.firebase.database.DatabaseReference
import com.google.gson.Gson
import com.ryuunta.iot.esp.widget.hideSoftKeyboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.RMainActivity
import ryuunta.iot.ryuuntaesp.adapter.WifiListAdapter
import ryuunta.iot.ryuuntaesp.core.base.BaseFragment
import ryuunta.iot.ryuuntaesp.core.base.Config
import ryuunta.iot.ryuuntaesp.data.model.DeviceObj
import ryuunta.iot.ryuuntaesp.data.model.ElementInfoObj
import ryuunta.iot.ryuuntaesp.data.model.WifiSSID
import ryuunta.iot.ryuuntaesp.databinding.FragmentAddDeviceBinding
import ryuunta.iot.ryuuntaesp.helper.ControlHelper
import ryuunta.iot.ryuuntaesp.helper.DeviceHelper
import ryuunta.iot.ryuuntaesp.main.home.devices.DeviceViewType
import ryuunta.iot.ryuuntaesp.utils.PermissionUtils.checkPermissionsNew
import ryuunta.iot.ryuuntaesp.utils.PermissionUtils.listPermission
import ryuunta.iot.ryuuntaesp.utils.RLog
import ryuunta.iot.ryuuntaesp.utils.randomId
import ryuunta.iot.ryuuntaesp.utils.scanESP8266Wifi
import ryuunta.iot.ryuuntaesp.utils.scanWifi
import ryuunta.iot.ryuuntaesp.utils.sendDataToESP8266
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick
import ryuunta.iot.ryuuntaesp.utils.showDialogNegative
import ryuunta.iot.ryuuntaesp.widget.StepView

class AddDeviceFragment : BaseFragment<FragmentAddDeviceBinding, AddDeviceViewModel>(
    FragmentAddDeviceBinding::inflate,
    AddDeviceViewModel::class.java
) {
    private val TAG = "AddDeviceActivity"


    private val pathDB = "esp8266"
    private lateinit var nodeLed: DatabaseReference
    private lateinit var nodeRelay1: DatabaseReference
    private lateinit var nodeRelay2: DatabaseReference
    private lateinit var relayEsp01: DatabaseReference
    private lateinit var doorLock: DatabaseReference

    private val controlHelper = ControlHelper()
    private val deviceHelper = DeviceHelper()

    private var listSSID: List<WifiSSID> = listOf()

    private var devId = ""

    private var mEsptouchTask: IEsptouchTask? = null

    private val stepView: StepView by lazy {
        StepView(requireContext())
    }


    private val ssidAdapter: WifiListAdapter by lazy {
        WifiListAdapter {
            binding.currentSSIDPickup = it
        }
    }

    override fun initViews(savedInstanceState: Bundle?) {
        (activity as RMainActivity).requestBluetooth()
        checkPermissionsNew(
            requireContext(),
            listPermission, {
//            if (it) {
                listSSID = scanWifi(requireContext())
                ssidAdapter.submitList(listSSID)

//            }
            },
            onCancel = { deniedPermissions ->
                deniedPermissions.forEach {
                    RLog.d(TAG, it.permissionName.toString())

                }

            })
        stepView.setStep(
            StepView.Step(binding.layoutStart),
            StepView.Step(binding.layoutFirst),
            StepView.Step(binding.layoutConfigEspDevice)
        )

        listSSID = scanWifi(requireContext())
        val listESP8266SSID = scanESP8266Wifi(requireContext())

        RLog.d(TAG, "Wifi list:  ${Gson().toJson(listSSID)}")
        RLog.d(TAG, "Wifi esp8266 list:  ${Gson().toJson(listESP8266SSID)}")
        ssidAdapter.submitList(listSSID)
        binding.rcvListSsid.adapter = ssidAdapter
//        initFirebase()


    }

    override fun initEvents() {
        super.initEvents()
        binding.btnNextStep.setPreventDoubleClick {
            when (stepView.currentStep) {
                binding.layoutStart.id -> {
                    createDeviceOnFirebase { devId ->
                        this.devId = devId
                        stepView.nextStep()
                        binding.btnNextStep.text = "Đi tiếp"
                    }
                }

                binding.layoutFirst.id -> {
                    stepView.nextStep()
                    binding.btnNextStep.text = getString(R.string.txt_done)
                }

                else -> {
                    navController.popBackStack()
                }
            }

        }
        binding.swiperRefresh.setOnRefreshListener {
            binding.swiperRefresh.isRefreshing = true
            listSSID = scanWifi(requireContext())
            ssidAdapter.submitList(listSSID)
            ssidAdapter.notifyDataSetChanged()
            binding.swiperRefresh.isRefreshing = false

        }

        binding.btnAuthorize.setOnClickListener {
            hideSoftKeyboard(requireActivity())

            val ssid = binding.currentSSIDPickup?.ssid.toString()
            val pwd = binding.edtInputPass.text.toString().trim()

            CoroutineScope(Dispatchers.IO).launch {
                val dataToSend =
                    "$ssid:$pwd:${Config.userUid}/houses/${Config.currentHouseId}/devices/$devId:"
                val espIp = "192.168.4.16"
                val espIp2 = "192.168.4.1"
                val port = 8888

//                sendDataToESP8266(requireContext(), dataToSend, espIp, port)
                sendDataToESP8266(requireContext(), dataToSend, espIp2, port)
            }


//            executeEspTouch()

//            CoroutineScope(Dispatchers.IO).launch {
//                // Xác thực SSID với password
//                val authenticated = WiFiAuthenticator().authenticate(ssid, pwd)
//
//                withContext(Dispatchers.Main) {
//                    // In kết quả xác thực
//                    if (authenticated) {
//                        binding.txtError.text = ""
//                        binding.txtError.visibility = View.GONE
//                        Log.i(TAG, "SSID được xác thực thành công")
//                        executeEsptouch()
//                    } else {
//                        binding.txtError.text = "Lỗi xác thực, thử lại!"
//                        binding.txtError.visibility = View.VISIBLE
//                        Log.e(TAG, "SSID không được xác thực")
//                    }
//                }
//            }
        }

        /* binding.apply {
             swLed.setOnCheckedChangeListener { _, isChecked ->
                 nodeLed.setValue(if (isChecked) 0 else 1)
                 binding.root.setBackgroundColor(if (isChecked) Color.BLUE else Color.WHITE)

             }
             swRelay1.setOnCheckedChangeListener { _, isChecked ->
                 nodeRelay1.setValue(if (isChecked) 0 else 1)
                 binding.root.setBackgroundColor(if (isChecked) Color.CYAN else Color.WHITE)
             }
             swRelay2.setOnCheckedChangeListener { _, isChecked ->
                 nodeRelay2.setValue(if (isChecked) 0 else 1)
                 binding.root.setBackgroundColor(if (isChecked) Color.RED else Color.WHITE)
             }

             swRelayEsp01.setOnCheckedChangeListener { _, isChecked ->
                 relayEsp01.setValue(if (isChecked) 0 else 1)
                 binding.root.setBackgroundColor(if (isChecked) Color.CYAN else Color.WHITE)
             }

             btnLockDoor.setOnClickListener {
                 doorLock.setValue(0)
             }
             btnOpenDoor.setOnClickListener {
                 doorLock.setValue(1)
             }

             button.setPreventDoubleClick {
                 val listElm = mutableMapOf<String, ElementInfoObj>()
                 for (i in 1..4) {
                     val randomId = randomId()
                     listElm[randomId] = ElementInfoObj(randomId, "Nút $i")
                 }
                 deviceHelper.addNewDevice(
                     DeviceObj(
                         randomId(),
                         "test",
                         null,
                         DeviceViewType.SWITCH_BUTTON.name,
                         listElm
                     )
                 ) {}
             }
         }*/
    }

    /*private fun initFirebase() {
        val db = FirebaseDatabase.getInstance()
        nodeLed = db.reference.child("fan_remote/level1")
        nodeLed.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = (snapshot.value as Long).toInt()
                Log.d("duongnk", "led state = $value")
                binding.swLed.isChecked = value == 0
//                Toast.makeText(
//                    requireContext(),
//                    "ESP LED is ${if (value == 0) "ON" else "OFF"}",
//                    Toast.LENGTH_SHORT
//                ).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("error_tag", error.message)
            }

        })

        nodeRelay1 = db.reference.child("$pathDB/switch/relay1")
        nodeRelay1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = (snapshot.value as Long).toInt()
                Log.d("duongnk", "switch 1 state = $value")
                binding.swRelay1.isChecked = value == 0
//                Toast.makeText(
//                    requireContext(),
//                    "SWITCH 1 is ${if (value == 0) "ON" else "OFF"}",
//                    Toast.LENGTH_SHORT
//                ).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("error_tag", error.message)
            }

        })

        nodeRelay2 = db.reference.child("$pathDB/switch/relay2")
        nodeRelay2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = (snapshot.value as Long).toInt()
                Log.d("duongnk", "switch 2 state = $value")
                binding.swRelay2.isChecked = value == 0
//                Toast.makeText(
//                    requireContext(),
//                    "SWITCH 2 is ${if (value == 0) "ON" else "OFF"}",
//                    Toast.LENGTH_SHORT
//                ).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("error_tag", error.message)
            }

        })

        relayEsp01 = db.reference.child("$pathDB/switch/relay_esp01")
        relayEsp01.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = (snapshot.value as Long).toInt()
                Log.d("duongnk", "switch esp 01 state = $value")
                binding.swRelayEsp01.isChecked = value == 0
//                Toast.makeText(
//                    requireContext(),
//                    "ESP 01 is ${if (value == 0) "ON" else "OFF"}",
//                    Toast.LENGTH_SHORT
//                ).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("error_tag", error.message)
            }

        })

        doorLock = db.reference.child(("$pathDB/stepper/door_lock"))
        doorLock.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = (snapshot.value as Long).toInt()
                Log.d("duongnk", "switch esp 01 state = $value")
//                Toast.makeText(
//                    requireContext(),
//                    "DOOR is ${if (value == 0) "Closed" else "Open"}",
//                    Toast.LENGTH_SHORT
//                ).show()
                if (value == 1) {
                    binding.apply {
                        btnOpenDoor.setBackgroundColor(Color.MAGENTA)
                        btnOpenDoor.isClickable = false
                        btnLockDoor.setBackgroundColor(Color.GRAY)
                        btnLockDoor.isClickable = true
                    }
                } else {
                    binding.apply {
                        btnLockDoor.setBackgroundColor(Color.MAGENTA)
                        btnLockDoor.isClickable = false
                        btnOpenDoor.setBackgroundColor(Color.GRAY)
                        btnOpenDoor.isClickable = true
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("error_tag", error.message)
            }

        })
    }*/

    /*private fun executeEspTouch() {
        val byteSSID = ByteUtil.getBytesByString(binding.currentSSIDPickup?.ssid)
        val pwdStr: String = binding.edtInputPass.text.toString().trim()
        val bytePwd = ByteUtil.getBytesByString((pwdStr))
        val bssid = TouchNetUtil.parseBssid2bytes(binding.currentSSIDPickup?.bssid)
//        val devCountStr: CharSequence = mBinding.deviceCountEdit.getText()
//        val deviceCount = devCountStr?.toString()?.toByteArray() ?: ByteArray(0)
        val broadcast: ByteArray = byteArrayOf(1)

        binding.pbLoading.visibility = View.VISIBLE
        binding.swiperRefresh.visibility = View.GONE

        mEsptouchTask?.interrupt()

        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG, "IEsptouchResult: ssid: ${binding.currentSSIDPickup?.ssid}\nPass: $pwdStr")
            mEsptouchTask = EsptouchTask(byteSSID, bssid, bytePwd, requireContext())
            mEsptouchTask?.setPackageBroadcast(true)
            mEsptouchTask?.setEsptouchListener { iEsptouchResult ->
                Log.d(TAG, "IEsptouchResult: ${Gson().toJson(iEsptouchResult)}")
            }
            val result = mEsptouchTask?.executeForResults(1)?.toList() ?: listOf()

            withContext(Dispatchers.Main) {
                binding.pbLoading.visibility = View.GONE
                binding.swiperRefresh.visibility = View.VISIBLE

                if (result.isEmpty()) {
                    Log.i(TAG, "IEsptouchResult: esp not found!!")
                    return@withContext
                }

                // check whether the task is cancelled and no results received
                val firstResult = result[0]
                if (firstResult.isCancelled) {
                    Log.i(TAG, "IEsptouchResult: first result = ${Gson().toJson(firstResult)}")
                    Log.i(TAG, "IEsptouchResult: task is cancelled and no results received")
                    return@withContext
                }

                // the task received some results including cancelled while
                // executing before receiving enough results
                if (!firstResult.isSuc) {
                    Log.i(TAG, "IEsptouchResult: task received some results including cancelled while executing before receiving enough results")
                    return@withContext
                }
                Log.i(TAG, "IEsptouchResult: first result = ${Gson().toJson(firstResult)}")
                Log.i(TAG, "IEsptouchResult: Success config ESP with: inetAddress= ${firstResult.inetAddress}\nbssid= ${firstResult.bssid}")

            }
        }
    }*/

    /* private fun cancelEsptouch() {
         mEsptouchTask?.interrupt()
     }*/

    private var idDeviceCache = ""

    private fun createDeviceOnFirebase(onComplete: (deviceId: String) -> Unit) {
        val listButton = mutableMapOf<String, ElementInfoObj>()
        if (binding.sw1Btn.isChecked) {
            listButton["1_btn"] = ElementInfoObj("1_btn", "Button 1", 0)
        }
        if (binding.sw4Btn.isChecked) {
            listButton["1_btn"] = ElementInfoObj("1_btn", "Button 1", 0)
            listButton["2_btn"] = ElementInfoObj("2_btn", "Button 2", 0)
            listButton["3_btn"] = ElementInfoObj("3_btn", "Button 3", 0)
            listButton["4_btn"] = ElementInfoObj("4_btn", "Button 4", 0)

        }
        idDeviceCache = randomId()
        val deviceObj = DeviceObj(
            idDeviceCache,
            "Switch ${System.currentTimeMillis().toString().takeLast(3)}",
            null,
            DeviceViewType.SWITCH_BUTTON.name,
            listButton
        )
        deviceHelper.addNewDevice(deviceObj, onComplete)
    }

    private fun confirmCancel() {
        requireContext().showDialogNegative(
            R.string.txt_cancel_add_device,
            R.string.txt_you_dont_to_want_add_device_anymore,
            lifecycle = lifecycle,
            lottieAnim = R.raw.anim_nahida_question,
            cancelRes = R.string.txt_no,
            isAnimLoop = true,
            confirmRes = R.string.txt_next,
            onCancel = {
                if (idDeviceCache.isNotEmpty()) {
                    deviceHelper.deleteDevice(idDeviceCache) {
                        navController.popBackStack()

                    }
                } else {
                    navController.popBackStack()
                }
            }
        )
    }

    override fun isCustomBackPress(): Boolean {
        return true
    }

    override fun customBackPress() {
        super.customBackPress()
//        cancelEsptouch()
        if (stepView.currentStep != binding.layoutStart.id) {
            confirmCancel()

        } else {
            navController.popBackStack()
        }

    }
}