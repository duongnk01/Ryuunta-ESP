package ryuunta.iot.ryuuntaesp.main.manage

import android.os.Bundle
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.core.base.BaseFragment
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

    private var currHouseId = ""
    private var currRoomId = ""
    private var currDevicesId = listOf<String>()

    override fun initViews(savedInstanceState: Bundle?) {
    }

    override fun initEvents() {

        binding.apply {
            btnCreateaHouse.setPreventDoubleClick {
                val rooms = mutableMapOf<String, HouseObj.RoomObj>()
                for (i in 1 until 5) {
                    val ranId = randomId()
                    val room = HouseObj.RoomObj(
                        ranId,
                        "Room $i"
                    )
                    rooms[ranId] = room
                }
                groupHelper.createHouse(
                    HouseObj(
                        randomId(),
                        "House 1",
                        rooms
                    )
                ) {
                    currHouseId = it
                    requireContext().showDialogNotification(
                        R.string.txt_done,
                        R.raw.anim_paimon_like,
                        lifecycle,
                        R.string.txt_house_created
                    ) {
                        refreshListData()
                    }
                }
            }

            btnCreataRoom.setPreventDoubleClick {
                groupHelper.addRoom(HouseObj.RoomObj(
                    randomId(),
                    "Room ${System.currentTimeMillis().toString().takeLast(4)}",
                    mapOf()
                )) {
                    currRoomId = it
                    requireContext().showDialogNotification(
                        R.string.txt_done,
                        R.raw.anim_paimon_like,
                        lifecycle,
                        R.string.txt_room_created
                    ) {
                        refreshListData()
                    }
                }

            }

            btnSignDevices.setPreventDoubleClick {
                deviceHelper.getDevicesByRoom { listDev ->
                    val listDevId = listDev.map {it.id}

                    groupHelper.signDevicesInRoom(currHouseId, currRoomId, listDevId ) { houseId, roomId, deviceIds ->
                        currHouseId = houseId
                        currRoomId = roomId
                        currDevicesId = deviceIds
                        requireContext().showDialogNotification(
                            R.string.txt_done,
                            R.raw.anim_paimon_like,
                            lifecycle,
                            R.string.txt_device_signed
                        ) {
                            refreshListData()
                        }
                    }
                }
            }
            btnChangeRoom.setPreventDoubleClick {
                groupHelper.getHouseById(currHouseId, onSuccess = {
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
                            refreshListData()
                        }
                    }

                }, onError = { code, message -> })
            }
        }
    }

    private fun refreshListData() {
        //refresh list house here
    }


    companion object {
        fun newInstance() = ManageFragment()
    }

}