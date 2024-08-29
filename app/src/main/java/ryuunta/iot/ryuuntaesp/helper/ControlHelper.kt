package ryuunta.iot.ryuuntaesp.helper

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ryuunta.iot.ryuuntaesp.core.base.Config
import ryuunta.iot.ryuuntaesp.data.model.ElementInfoObj
import ryuunta.iot.ryuuntaesp.data.model.UserInfo
import ryuunta.iot.ryuuntaesp.helper.FirebaseRealtimeHelper
import ryuunta.iot.ryuuntaesp.utils.RLog

class ControlHelper {
    private val TAG = FirebaseRealtimeHelper.TAG
    private val ref = FirebaseRealtimeHelper()
    private val db: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    private val userRef = db.getReference(DBNode.USERS.path)
    private val myRef = db.getReference(DBNode.USERS.path).child(Config.userUid).child(DBNode.DEVICES.path)

    fun generateUserData(
        uid: String,
        user: UserInfo,
        onSuccess: (UserInfo) -> Unit = {},
        onFailure: (code: Int, message: String) -> Unit = { code, message -> }
    ) {
        RLog.d(TAG, "fetch user data from node")
        ref.getUsersReference().child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    RLog.d(TAG, "create new user")
                    userRef.child(uid).setValue(user)
                }
                Config.userUid = uid
                onSuccess(user)
            }

            override fun onCancelled(error: DatabaseError) {
                RLog.d(TAG, "onCancelled: ${error.message}")
                onFailure(-2, error.message)
            }

        })

    }

    /**
     * Function to control data realtime firebase
     * @param deviceId : device's path in firebase realtime database
     * @param elements : list of elements' of device in firebase realtime database
     * @param state : if state is not null -> function will send state to firebase otherwise will get state from firebase to update UI
     */
    fun controlDevice(
        deviceId: String,
        elements: Map<String, ElementInfoObj>,
        state: Boolean?,
        onStateUpdated: (ElementInfoObj) -> Unit = { elm -> },
        onError: (code: Int, message: String) -> Unit = { code, message -> }
    ) {
        if (elements.isEmpty()) {
            RLog.e(TAG, "elementPath is empty")
            return
        }
        elements.forEach { (key, element) ->
            val myElmRef = ref.getElementReference(deviceId, key)
            if (state == null) {
                myElmRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val elm = snapshot.getValue(ElementInfoObj::class.java)
                        RLog.d(TAG, "${elm?.label} state = ${elm?.value}")
                        if (elm != null) {
                            onStateUpdated(elm)
                        } else {
                            onError(-1, "Element is null")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        onError(error.code, error.message)
                        RLog.d(
                            TAG,
                            "Data realtime cancelled: \nerror code: ${error.code}\nerror message: ${error.message}\nerror details: ${error.details}"
                        )
                    }

                })
            } else {
                element.value = if (state) 1 else 0
                myElmRef.setValue(element)
                onStateUpdated(element)
            }
        }

//        elements.forEach { element ->
//            val node = db.reference.child("$deviceId/$element")
//            if (state == null) {
//                node.addValueEventListener(object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        try {
//                            val value = (snapshot.value as Long).toInt()
//                            RLog.d(TAG, "$deviceId/$element state = $value")
//                            onStateUpdated(element, value == 1)
//                        } catch (e: Exception) {
//                            e.printStackTrace()
//                        }
//
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        onError(error.code, error.message)
//                        RLog.d(
//                            TAG,
//                            "Data realtime cancelled: \nerror code: ${error.code}\nerror message: ${error.message}\nerror details: ${error.details}"
//                        )
//                    }
//
//                })
//            } else {
//                node.setValue(if (state) 1 else 0)
//                onStateUpdated(element, state)
//            }
//
//        }

    }

    /**
     * addValueEventListener() : get an observable of data from firebase realtime database
     * addListenerForSingleValueEvent() : get data and stop listening from firebase realtime database
     */
    /*fun getDataFromNode(targetNode: String) {
        val node = db.getReference(targetNode)
        node.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //all data of child node here
                for (childSnapshot in snapshot.children)    //get all child node
                {
                    val childKey = childSnapshot.key        //get key of child node
                    val childValue =
                        childSnapshot.getValue(RItem::class.java)    //get value of child node and parse to RItem object
                    //do something with childKey and childValue
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //handler error
            }

        })
    }

    fun exampleQuerryDatabase() {
        val node = db.getReference("users")

        //add a new child node in users")
        node.child("key").setValue("value")
            .addOnCompleteListener { }
            .addOnFailureListener { }

        //add a child node with unique key
        val user = "Alice"
        val newChildRef = node.push()
        newChildRef.setValue(user)

        //add many child node in users
        val newUsers = mapOf(
            "user1" to "Alice",
            "user2" to "Bob"
        )
        node.updateChildren(newUsers)

        //update name of user1 in users node
        node.child("user1").child("name").setValue("Alice")
            .addOnCompleteListener { }
            .addOnFailureListener { }

        //delete user1 in users node
        node.child("user1").removeValue()
            .addOnCompleteListener { }
            .addOnFailureListener { }
    }*/

}