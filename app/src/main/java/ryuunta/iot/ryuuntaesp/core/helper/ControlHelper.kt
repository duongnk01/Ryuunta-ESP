package ryuunta.iot.ryuuntaesp.core.helper

import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ryuunta.iot.ryuuntaesp.utils.RLog

class ControlHelper {
    private val TAG = "ControlHelper"
    private val dataRealtime = FirebaseDatabase.getInstance()

    /**
     * Function to control data realtime firebase
     * @param devicePath : device's path in firebase realtime database
     * @param elementsPath : list of elements' path of device in firebase realtime database
     * @param state : if state is not null -> function will send state to firebase otherwise will get state from firebase to update UI
     */
    fun controlDevice(
        devicePath: String,
        elementsPath: List<String>,
        state: Boolean?,
//        onStateChanged: (String, Boolean) -> Unit = {elmPath, isOn -> },
        onStateUpdated: (String, Boolean) -> Unit = {elmPath, isOn -> }
    ) {
        if (elementsPath.isEmpty()) {
            RLog.e(TAG, "elementPath is empty")
            return
        }

        elementsPath.forEach { element ->
            val node = dataRealtime.reference.child("$devicePath/$element")
            if (state == null) {
                node.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        try {
                            val value = (snapshot.value as Long).toInt()
                            RLog.d(TAG, "$devicePath/$element state = $value")
                            onStateUpdated(element, value == 1)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {

                        RLog.d(
                            TAG,
                            "Data realtime cancelled: \nerror code: ${error.code}\nerror message: ${error.message}\nerror details: ${error.details}"
                        )
                    }

                })
            } else {
                node.setValue(if (state) 1 else 0)
                onStateUpdated(element, state)
            }

        }

    }
}