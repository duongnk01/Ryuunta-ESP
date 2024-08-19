package ryuunta.iot.ryuuntaesp.home

import ryuunta.iot.ryuuntaesp.base.BaseViewModel
import ryuunta.iot.ryuuntaesp.data.model.RoomObj
import ryuunta.iot.ryuuntaesp.data.network.RetrofitService

class HomeViewModel() : BaseViewModel() {

    val roomList = listOf(
        RoomObj(0, "Tất cả"),
        RoomObj(1, "Phòng khách"),
        RoomObj(2, "Nhà bếp"),
        RoomObj(3, "Gara"),
        RoomObj(4, "Phòng ngủ")
    )

}