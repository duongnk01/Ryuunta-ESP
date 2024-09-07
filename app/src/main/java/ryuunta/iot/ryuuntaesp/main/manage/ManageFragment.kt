package ryuunta.iot.ryuuntaesp.main.manage

import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.adapter.HouseManageAdapter
import ryuunta.iot.ryuuntaesp.core.base.BaseFragment
import ryuunta.iot.ryuuntaesp.core.base.Config
import ryuunta.iot.ryuuntaesp.data.model.HouseObj
import ryuunta.iot.ryuuntaesp.databinding.FragmentManageBinding
import ryuunta.iot.ryuuntaesp.helper.DeviceHelper
import ryuunta.iot.ryuuntaesp.helper.GroupHelper
import ryuunta.iot.ryuuntaesp.main.home.devices.DeviceViewType
import ryuunta.iot.ryuuntaesp.utils.RLog
import ryuunta.iot.ryuuntaesp.utils.developInProgress
import ryuunta.iot.ryuuntaesp.utils.randomId
import ryuunta.iot.ryuuntaesp.utils.sendDataToESP8266
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick
import ryuunta.iot.ryuuntaesp.utils.showDialogError
import ryuunta.iot.ryuuntaesp.utils.showDialogNotification

class ManageFragment : BaseFragment<FragmentManageBinding, ManageViewModel>(
    FragmentManageBinding::inflate,
    ManageViewModel::class.java
) {

    private val houseManageAdapter: HouseManageAdapter by lazy {
        HouseManageAdapter {
            //navigate to house detail
            requireContext().developInProgress(lifecycle)
        }
    }

    override fun initViews(savedInstanceState: Bundle?) {
        binding.apply {
            rcvListHome.adapter = houseManageAdapter
        }
    }

    override fun onSetViewInfo() {
        super.onSetViewInfo()
        viewModel.fetchHouseData()

        viewModel.listHouse.observe(viewLifecycleOwner) {
            houseManageAdapter.submitList(it)
        }
    }

    override fun initEvents() {

        binding.apply {

        }
    }


    companion object {
        fun newInstance() = ManageFragment()
    }

}