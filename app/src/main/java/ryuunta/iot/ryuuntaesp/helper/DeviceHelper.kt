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

    fun addNewDevice(device: DeviceObj, onCompleted: (deviceId: String) -> Unit, onFailure: (message: String?) -> Unit) {
        ref.getDeviceReference(device.id).setValue(device)
            .addOnCompleteListener {
                onCompleted(device.id)
            }
            .addOnFailureListener { onFailure(it.message) }
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
            .addValueEventListener(object : ValueEventListener {
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

    fun getDeviceName(deviceId: String, onCompleted: (String) -> Unit) {
        ref.getDeviceReference(deviceId).child("label").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                onCompleted(snapshot.value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
            }
        })
    }

    fun changeNameDevice(deviceId: String, newName: String, onCompleted: () -> Unit) {
        ref.getDevicesReference().child(deviceId).updateChildren(mapOf("label" to newName))
            .addOnCompleteListener {
                onCompleted()
            }

    }

    fun changeNameElement(deviceId: String, elementId: String, newName: String, onCompleted: () -> Unit) {
        ref.getDevicesReference().child(deviceId).child(DBNode.BUTTON_LIST.path).child(elementId).updateChildren(mapOf("label" to newName))
            .addOnCompleteListener {
                onCompleted()
            }
    }

    fun deleteDevice(idDevice: String, onCompleted: () -> Unit) {
        ref.getDeviceReference(idDevice).removeValue().addOnCompleteListener {
            onCompleted()
        }
    }

}