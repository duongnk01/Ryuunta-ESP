package ryuunta.iot.ryuuntaesp.helper

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ryuunta.iot.ryuuntaesp.core.base.Config
import ryuunta.iot.ryuuntaesp.data.model.DeviceObj
import ryuunta.iot.ryuuntaesp.helper.DatabaseNode.DEVICES
import ryuunta.iot.ryuuntaesp.helper.DatabaseNode.USERS

class DeviceHelper {

    private val db: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }
    private val userRef = db.getReference(USERS).child(Config.userUid)
    private val myRef = db.getReference(USERS).child(Config.userUid).child(DEVICES)

    fun addNewDevice(device: DeviceObj) {
        myRef.child(device.id).setValue(device)
    }


//    fun getAllDevices(): List<DeviceObj> = listOf(
//        DeviceObj(0, "Điều khiển quạt", "fan_remote", DeviceViewType.FAN_REMOTE, hashMapOf(0 to "swing", 1 to "level1", 2 to "level2")),
//        DeviceObj(1, "Công tắc", "switch_button", DeviceViewType.SWITCH_BUTTON, hashMapOf(0 to "button1")),
//        DeviceObj(2, "Công tắc 3 nút", "fan_remote", DeviceViewType.SWITCH_BUTTON, hashMapOf(0 to "swing", 1 to "level1", 2 to "level2")),
//        DeviceObj(3, "Công tắc 4 nút", "switch_4_button", DeviceViewType.SWITCH_BUTTON, hashMapOf(0 to "button1", 1 to "button2", 2 to "button3", 3 to "button4")),
//        DeviceObj(4, "Công tắc 5 nút", "switch_5_button", DeviceViewType.SWITCH_BUTTON, hashMapOf(0 to "button1", 1 to "button2", 2 to "button3", 3 to "button4", 4 to "button5")),
//        DeviceObj(5, "Công tắc 6 nút", "switch_6_button", DeviceViewType.SWITCH_BUTTON, hashMapOf(0 to "button1", 1 to "button2", 2 to "button3", 3 to "button4", 4 to "button5", 5 to "button6")),
//        DeviceObj(6, "Công tắc 6 nút", "switch_6_button", DeviceViewType.SWITCH_BUTTON, hashMapOf(0 to "button1", 1 to "button2", 2 to "button3", 3 to "button4", 4 to "button5", 5 to "button6")),
//        DeviceObj(7, "Công tắc 6 nút", "switch_6_button", DeviceViewType.SWITCH_BUTTON, hashMapOf(0 to "button1", 1 to "button2", 2 to "button3", 3 to "button4", 4 to "button5", 5 to "button6")),
//        DeviceObj(8, "Công tắc 6 nút", "switch_6_button", DeviceViewType.SWITCH_BUTTON, hashMapOf(0 to "button1", 1 to "button2", 2 to "button3", 3 to "button4", 4 to "button5", 5 to "button6")),
//        DeviceObj(9, "Công tắc 6 nút", "switch_6_button", DeviceViewType.SWITCH_BUTTON, hashMapOf(0 to "button1", 1 to "button2", 2 to "button3", 3 to "button4", 4 to "button5", 5 to "button6")),
//        DeviceObj(10, "Công tắc 6 nút", "switch_6_button", DeviceViewType.SWITCH_BUTTON, hashMapOf(0 to "button1", 1 to "button2", 2 to "button3", 3 to "button4", 4 to "button5", 5 to "button6")),
//
//
//
//    )

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
//                    val dev = DeviceObj()
//                    dev.apply {
//                        id = childNode.key ?: ""
//                        label = childNode.child("label").value.toString()
//                        type = DeviceViewType.valueOf(childNode.child("type").value.toString())
//                        val elms = mutableMapOf<String, ElementInfoObj>()
//                        for (child in childNode.child("buttonList").children) {
//                            child.key?.let {
//                                elms[it] = ElementInfoObj(child.child("label").value.toString(), child.child("value").value.toString().toInt())
//
//                            }
//                        }
//                        buttonList = elms
//                    }

                }
                onCompleted(deviceList)
            }

            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
            }

        })
    }

    fun getDeviceById(deviceId: String, onCompleted: (DeviceObj) -> Unit) {
        myRef.child(deviceId).addValueEventListener(object : ValueEventListener {
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