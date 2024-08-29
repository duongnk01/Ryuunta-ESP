package ryuunta.iot.ryuuntaesp.helper

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ryuunta.iot.ryuuntaesp.core.base.Config
import ryuunta.iot.ryuuntaesp.data.model.DeviceObj
import ryuunta.iot.ryuuntaesp.data.model.ElementInfoObj

class DeviceHelper {

    private val TAG = FirebaseRealtimeHelper.TAG
    private val ref = FirebaseRealtimeHelper()

    private val db: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }
    private val userRef = db.getReference(DBNode.USERS.path).child(Config.userUid)
    private val myRef =
        db.getReference(DBNode.USERS.path).child(Config.userUid).child(DBNode.DEVICES.path)

    fun addNewDevice(device: DeviceObj, onCompleted: () -> Unit) {
        ref.getDeviceReference(device.id).setValue(device)
            .addOnCompleteListener {
                onCompleted()
            }
    }

    fun getAllDevices(onCompleted: (List<DeviceObj>) -> Unit) {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val deviceList = mutableListOf<DeviceObj>()

                for (childNode in snapshot.children) {
                    val dev = childNode.getValue(DeviceObj::class.java)
                    dev?.let {
//                        dev.id = childNode.key ?: ""
                        deviceList.add(it)
                    }

                }
                onCompleted(deviceList)
            }

            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
            }

        })
    }

    //get all device of house and filter by condition, roomId =  is mean get all device of house
    fun getDevicesByRoom(
        roomId: String = "0",
        onCompleted: (List<DeviceObj>) -> Unit
    ) {
        ref.getDevicesReference()
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val deviceList = mutableListOf<DeviceObj>()
                    if (roomId == "0") {
                        for (childNode in snapshot.children) {
                            val dev = childNode.getValue(DeviceObj::class.java)
                            dev?.let {
                                deviceList.add(it)
                            }
                        }
                    } else {
                        for (childNode in snapshot.children) {
                            val dev = childNode.getValue(DeviceObj::class.java)
                            dev?.let {
                                if (it.roomId == roomId) {
                                    deviceList.add(it)
                                }
                            }
                        }
                    }

                    onCompleted(deviceList.sortedByDescending { it.id.split('+')[0].toLong() })        //sort by timestamp it added at first of id
                }

                override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
                }

            })
    }

    fun getDeviceById(deviceId: String, onCompleted: (DeviceObj) -> Unit) {
        ref.getDeviceReference(deviceId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(DeviceObj::class.java)?.let {
                    it.id = deviceId
                    onCompleted(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
            }

        })
    }

}