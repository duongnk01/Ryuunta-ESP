package ryuunta.iot.ryuuntaesp.dialog

import android.content.Context
import android.os.Handler
import android.os.Looper
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.adapter.WifiListAdapter
import ryuunta.iot.ryuuntaesp.common.RyuuntaDialog
import ryuunta.iot.ryuuntaesp.data.model.WifiSSID
import ryuunta.iot.ryuuntaesp.databinding.DialogWifiSelectionBinding
import ryuunta.iot.ryuuntaesp.utils.gone
import ryuunta.iot.ryuuntaesp.utils.scanESP8266Wifi
import ryuunta.iot.ryuuntaesp.utils.scanWifi
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick
import ryuunta.iot.ryuuntaesp.utils.show

class DialogWifiSelection(
    context: Context,
    private val onSelected: (wifi: WifiSSID) -> Unit
) : RyuuntaDialog<DialogWifiSelectionBinding>(context) {

    private var listSSID: List<WifiSSID> = listOf()

    private val ssidAdapter: WifiListAdapter by lazy {
        WifiListAdapter {
            onSelected(it)
            onDismissAndRemoveRes()
        }
    }

    override fun onDialogShown() {
        super.onDialogShown()

        binding.rcvListSsid.adapter = ssidAdapter
        checkWifiConnection()

    }

    override fun onCreate() {

        binding.btnRefresh.setPreventDoubleClick {
            binding.animLoading.show()
            binding.rcvListSsid.gone()
            binding.imgEmpty.gone()
            Handler(Looper.getMainLooper()).postDelayed({
                checkWifiConnection()
            }, 1000)

        }
    }

    private fun checkWifiConnection() {
        binding.animLoading.gone()
        listSSID = scanESP8266Wifi(context)
        if (listSSID.isNotEmpty()) {
            binding.rcvListSsid.show()
            binding.imgEmpty.gone()
            ssidAdapter.submitList(listSSID)
        } else {
            binding.rcvListSsid.gone()
            binding.imgEmpty.show()
        }
    }

    override fun isCanceledOnTouchOutside(): Boolean = true

    override fun getLayoutId(): Int = R.layout.dialog_wifi_selection

    override fun initBinding(): DialogWifiSelectionBinding =
        DialogWifiSelectionBinding.bind(contentView)
}