package ryuunta.iot.ryuuntaesp.main.manage

import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.core.base.BaseFragment
import ryuunta.iot.ryuuntaesp.core.base.Config
import ryuunta.iot.ryuuntaesp.data.model.HouseObj
import ryuunta.iot.ryuuntaesp.databinding.FragmentManageBinding
import ryuunta.iot.ryuuntaesp.helper.DeviceHelper
import ryuunta.iot.ryuuntaesp.helper.GroupHelper
import ryuunta.iot.ryuuntaesp.main.home.devices.DeviceViewType
import ryuunta.iot.ryuuntaesp.utils.RLog
import ryuunta.iot.ryuuntaesp.utils.randomId
import ryuunta.iot.ryuuntaesp.utils.sendDataToESP8266
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick
import ryuunta.iot.ryuuntaesp.utils.showDialogError
import ryuunta.iot.ryuuntaesp.utils.showDialogNotification

class ManageFragment : BaseFragment<FragmentManageBinding, ManageViewModel>(
    FragmentManageBinding::inflate,
    ManageViewModel::class.java
) {

    private val groupHelper = GroupHelper()
    private val deviceHelper = DeviceHelper()

    private var currRoomId = "${System.currentTimeMillis()}+${Config.userUid}"
    private var currDevicesId = listOf<String>()

    override fun initViews(savedInstanceState: Bundle?) {
    }

    override fun initEvents() {

        binding.apply {
            btnChangeNameHouse.setPreventDoubleClick {
                val newName = edtNewNameHouse.text?.trim()
                if (!newName.isNullOrEmpty()) {
                    groupHelper.changeNameHouse(newName.toString()) {
                        requireContext().showDialogNotification(
                            R.string.txt_done,
                            R.raw.anim_paimon_like,
                            lifecycle,
                            R.string.txt_new_house_updated
                        ) {
//                        refreshListData()
                        }
                    }
                }

            }

            btnChangeNameRoom.setPreventDoubleClick {
                val newName = edtNewNameHouse.text?.trim()
                if (!newName.isNullOrEmpty()) {
                    groupHelper.changeRoomName(currRoomId, newName.toString()) {
                        requireContext().showDialogNotification(
                            R.string.txt_done,
                            R.raw.anim_paimon_like,
                            lifecycle,
                            R.string.txt_new_room_updated
                        )
                    }
                }
            }

            btnCreataRoom.setPreventDoubleClick {
                groupHelper.addRoom(HouseObj.RoomObj(
                    randomId(),
                    "Room ${System.currentTimeMillis().toString().takeLast(3)}",
                    mapOf()
                )) {
                    currRoomId = it
                    requireContext().showDialogNotification(
                        R.string.txt_done,
                        R.raw.anim_paimon_like,
                        lifecycle,
                        R.string.txt_room_created
                    ) {
//                        updateHome.updateRoom()
                    }
                }

            }

            btnSendUdp.setPreventDoubleClick {
                CoroutineScope(Dispatchers.IO).launch {
                    val dataToSend = "Duongnk:10101010:qbxZxgTnS1SxRP8xR80blXUjXKy2/houses/1724860403770+7736fc7a-0f0a-4acf-a659-48b673171e5f/devices/1725529993959+b36925cf-d56b-4207-a969-28bfed12e13f:"
                    val espIp2 = Config.softAPIPAddress
                    val port = Config.espPort

                    sendDataToESP8266(dataToSend, espIp2, port, { isSuccess ->
                        //on received boolean
                        if (isSuccess) {

                            //connect thành công
                            RLog.d("duongnk", "connect thành công")

                        } else {
                            //connect thất bại
                            RLog.d("duongnk", "connect thất bại")


                        }

                    }, onError = { errorMess ->
                        requireContext().showDialogError(
                            lifecycle,
                            errorMess
                        ) {
                        }

                    })
                }
            }
        }
    }


    companion object {
        fun newInstance() = ManageFragment()
    }

}