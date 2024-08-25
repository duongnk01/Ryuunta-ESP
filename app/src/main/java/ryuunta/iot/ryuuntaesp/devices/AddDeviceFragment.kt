package ryuunta.iot.ryuuntaesp.devices

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import com.espressif.iot.esptouch.EsptouchTask
import com.espressif.iot.esptouch.IEsptouchTask
import com.espressif.iot.esptouch.util.ByteUtil
import com.espressif.iot.esptouch.util.TouchNetUtil
import com.google.gson.Gson
import com.ryuunta.iot.esp.widget.hideSoftKeyboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ryuunta.iot.ryuuntaesp.adapter.WifiListAdapter
import ryuunta.iot.ryuuntaesp.core.base.BaseFragment
import ryuunta.iot.ryuuntaesp.data.model.WifiSSID
import ryuunta.iot.ryuuntaesp.databinding.FragmentAddDeviceBinding
import ryuunta.iot.ryuuntaesp.utils.PermissionUtils.checkPermissions
import ryuunta.iot.ryuuntaesp.utils.scanWifi

class AddDeviceFragment: BaseFragment<FragmentAddDeviceBinding, AddDeviceViewModel>(FragmentAddDeviceBinding::inflate, AddDeviceViewModel::class.java) {
    private val TAG = "AddDeviceActivity"

    private val listPermission: List<String>
        get() = if (Build.VERSION.SDK_INT == Build.VERSION_CODES.S)
            listOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            )
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            listOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS
            )
        else
            listOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE
            )

    private var listSSID: List<WifiSSID> = listOf()

    private var mEsptouchTask: IEsptouchTask? = null


    private val ssidAdapter: WifiListAdapter by lazy {
        WifiListAdapter {
            binding.currentSSIDPickup = it
        }
    }
    override fun initViews(view: Bundle?) {
        checkPermissions(
            requireContext(),
            listPermission
        )

        listSSID = scanWifi(requireContext())
        ssidAdapter.submitList(listSSID)
        binding.rcvListSsid.adapter = ssidAdapter

        binding.swiperRefresh.setOnRefreshListener {
            binding.swiperRefresh.isRefreshing = true
            listSSID = scanWifi(requireContext())
            ssidAdapter.submitList(listSSID)
            binding.swiperRefresh.isRefreshing = false

        }

        binding.btnAuthorize.setOnClickListener {
            val ssid = binding.currentSSIDPickup?.ssid.toString()
            val pwd = binding.edtInputPass.text.toString().trim()
            hideSoftKeyboard(requireActivity())
            executeEspTouch()

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
    }

    private fun executeEspTouch() {
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
    }

    private fun cancelEsptouch() {
        mEsptouchTask?.interrupt()
    }

    override fun isCustomBackPress(): Boolean {
        return true
    }

    override fun customBackPress() {
        super.customBackPress()
        cancelEsptouch()
        navController.popBackStack()

    }
}