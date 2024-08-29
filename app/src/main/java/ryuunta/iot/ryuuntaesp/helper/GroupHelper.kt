package ryuunta.iot.ryuuntaesp.helper

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import ryuunta.iot.ryuuntaesp.core.base.Config.currentHouseId
import ryuunta.iot.ryuuntaesp.data.model.HouseObj
import ryuunta.iot.ryuuntaesp.utils.RLog

class GroupHelper {
    private val TAG = FirebaseRealtimeHelper.TAG
    private val ref = FirebaseRealtimeHelper()


    fun createHouse(houseObj: HouseObj, onSuccess: (houseId:String) -> Unit) {
        val mapHouseObj = mapOf(houseObj.id to houseObj)
        ref.getHousesReference().child(houseObj.id).setValue(houseObj)
            .addOnCompleteListener { onSuccess(houseObj.id) }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    fun addRoom(roomObj: HouseObj.RoomObj, onSuccess: (roomId: String) -> Unit) {
        ref.getRoomReference(roomObj.id).setValue(roomObj)
            .addOnCompleteListener { onSuccess(roomObj.id) }
    }

    fun getHouseList(onCompleted: (List<HouseObj>) -> Unit) {
        ref.getHousesReference().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val houses = mutableListOf<HouseObj>()
                for (snap in snapshot.children) {
                    val house = snap.getValue(HouseObj::class.java)
                    house?.let {
                        houses.add(it)
                    }
                }
                onCompleted(houses.sortedBy { it.id.split('+')[0].toLong() })        //sort by timestamp it added at first of id
            }

            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
            }

        })
    }

    fun getHouseById(
        houseId: String,
        onSuccess: (HouseObj) -> Unit,
        onError: (code: Int, message: String) -> Unit
    ) {
        ref.getHouseReference().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val house = snapshot.getValue(HouseObj::class.java)
                house?.let(onSuccess)
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.code, error.message)
            }

        })
    }

    fun getRoomsInHouse(onCompleted: (List<HouseObj.RoomObj>) -> Unit) {
        ref.getRoomsReference().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val rooms = mutableListOf<HouseObj.RoomObj>()
                for (snap in snapshot.children) {
                    val room = snap.getValue(HouseObj.RoomObj::class.java)
                    room?.let {
                        rooms.add(it)
                    }
                }
                onCompleted(rooms.sortedBy { it.id.split('+')[0].toLong() })        //sort by timestamp it added at first of id
            }

            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
            }

        })
    }

    fun signDevicesInRoom(
        houseId: String,
        roomId: String,
        deviceIds: List<String>,
        onSuccess: (houseId: String, roomId: String, deviceIds: List<String>) -> Unit
    ) {
        ref.getRoomReference(roomId).child(DBNode.DEVICES_ID_SIGNED.path)
            .setValue(deviceIds).addOnCompleteListener { onSuccess(houseId, roomId, deviceIds) }
    }

    fun changeRoom(
        deviceId: String,
        oldRoomId: String,
        newRoomId: String,
        onSuccess: () -> Unit
    ) {
        //delete device if from old room
        ref.getRoomReference(oldRoomId).child(deviceId).removeValue()

        //add device to new room
        ref.getRoomReference(newRoomId).child(deviceId).setValue(deviceId)
            .addOnCompleteListener { onSuccess() }
    }
}