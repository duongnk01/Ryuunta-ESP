package ryuunta.iot.ryuuntaesp.main.manage

import android.os.Bundle
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.core.base.BaseFragment
import ryuunta.iot.ryuuntaesp.core.base.Config
import ryuunta.iot.ryuuntaesp.data.model.HouseObj
import ryuunta.iot.ryuuntaesp.databinding.FragmentManageBinding
import ryuunta.iot.ryuuntaesp.helper.DeviceHelper
import ryuunta.iot.ryuuntaesp.helper.GroupHelper
import ryuunta.iot.ryuuntaesp.utils.randomId
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick
import ryuunta.iot.ryuuntaesp.utils.showDialogNotification

class ManageFragment : BaseFragment<FragmentManageBinding, ManageViewModel>(
    FragmentManageBinding::inflate,
    ManageViewModel::class.java
) {

    private val groupHelper = GroupHelper()
    private val deviceHelper = DeviceHelper()

    private var currRoomId = ""
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

            btnSignDevices.setPreventDoubleClick {
                deviceHelper.getDevicesByRoom { listDev ->
                    val listDevId = listDev.map {it.id}

                    groupHelper.signDevicesInRoom(currRoomId, listDevId ) { roomId, deviceIds ->
                        currRoomId = roomId
                        currDevicesId = deviceIds
                        requireContext().showDialogNotification(
                            R.string.txt_done,
                            R.raw.anim_paimon_like,
                            lifecycle,
                            R.string.txt_device_signed
                        ) {
//                            refreshHomeData()
                        }
                    }
                }
            }
            btnChangeRoom.setPreventDoubleClick {
                groupHelper.getHouseById(Config.currentHouseId, onSuccess = {
                    val rooms = it.rooms
                    var newRoomId = rooms.keys.random()
                    while (newRoomId == currRoomId) {
                        newRoomId = rooms.keys.random()
                    }
                    groupHelper.changeRoom(currDevicesId[0], currRoomId, newRoomId) {
                        requireContext().showDialogNotification(
                            R.string.txt_done,
                            R.raw.anim_paimon_like,
                            lifecycle,
                            R.string.txt_device_signed
                        ) {
//                            refreshHomeData()
                        }
                    }

                }, onError = { code, message -> })
            }
        }
    }


    companion object {
        fun newInstance() = ManageFragment()
    }

}