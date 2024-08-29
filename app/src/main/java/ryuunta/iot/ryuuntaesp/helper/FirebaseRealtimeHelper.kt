package ryuunta.iot.ryuuntaesp.helper

import com.google.firebase.database.FirebaseDatabase
import ryuunta.iot.ryuuntaesp.core.base.Config

class FirebaseRealtimeHelper {

    private val db = FirebaseDatabase.getInstance()

    private val users = db.getReference(DBNode.USERS.path)
    private val user = db.getReference(DBNode.USERS.path).child(Config.userUid)
    private val devices = db.getReference(DBNode.USERS.path).child(Config.userUid).child(DBNode.DEVICES.path)

    fun getUsersReference() = users

    fun getUserInfoReference() = user


    fun getHousesReference() = user.child(DBNode.HOUSES.path)

    fun getHouseReference() = user.child(DBNode.HOUSES.path).child(Config.currentHouseId)

    fun getDevicesReference() = getHouseReference().child(DBNode.DEVICES.path)

    fun getDeviceReference(deviceId: String) = getDevicesReference().child(deviceId)

    fun getElementsReference(deviceId: String) = getDeviceReference(deviceId).child(DBNode.BUTTON_LIST.path)

    fun getElementReference(deviceId: String, elementId: String) = getElementsReference(deviceId).child(elementId)

    fun getRoomsReference() = getHouseReference().child(DBNode.ROOMS.path)

    fun getRoomReference(roomId: String) = getRoomsReference().child(roomId)

    companion object {
        const val TAG = "FirebaseRealtimeHelper"
    }

}

