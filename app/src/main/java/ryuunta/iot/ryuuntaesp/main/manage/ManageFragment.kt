package ryuunta.iot.ryuuntaesp.main.manage

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ryuunta.iot.ryuuntaesp.core.base.BaseFragment
import ryuunta.iot.ryuuntaesp.data.model.DeviceObj
import ryuunta.iot.ryuuntaesp.data.model.ElementInfoObj
import ryuunta.iot.ryuuntaesp.databinding.FragmentManageBinding
import ryuunta.iot.ryuuntaesp.helper.ControlHelper
import ryuunta.iot.ryuuntaesp.helper.DeviceHelper
import ryuunta.iot.ryuuntaesp.main.home.devices.DeviceViewType
import ryuunta.iot.ryuuntaesp.utils.randomId
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick

class ManageFragment : BaseFragment<FragmentManageBinding, ManageViewModel>(
    FragmentManageBinding::inflate,
    ManageViewModel::class.java
) {

    private val pathDB = "esp8266"
    private lateinit var nodeLed: DatabaseReference
    private lateinit var nodeRelay1: DatabaseReference
    private lateinit var nodeRelay2: DatabaseReference
    private lateinit var relayEsp01: DatabaseReference
    private lateinit var doorLock: DatabaseReference

    private val controlHelper = ControlHelper()
    private val deviceHelper = DeviceHelper()

    override fun initViews(savedInstanceState: Bundle?) {
        initFirebase()
    }

    override fun initEvents() {
        binding.apply {
            swLed.setOnCheckedChangeListener { _, isChecked ->
                nodeLed.setValue(if (isChecked) 0 else 1)
                binding.root.setBackgroundColor(if (isChecked) Color.BLUE else Color.WHITE)

            }
            swRelay1.setOnCheckedChangeListener { _, isChecked ->
                nodeRelay1.setValue(if (isChecked) 0 else 1)
                binding.root.setBackgroundColor(if (isChecked) Color.CYAN else Color.WHITE)
            }
            swRelay2.setOnCheckedChangeListener { _, isChecked ->
                nodeRelay2.setValue(if (isChecked) 0 else 1)
                binding.root.setBackgroundColor(if (isChecked) Color.RED else Color.WHITE)
            }

            swRelayEsp01.setOnCheckedChangeListener { _, isChecked ->
                relayEsp01.setValue(if (isChecked) 0 else 1)
                binding.root.setBackgroundColor(if (isChecked) Color.CYAN else Color.WHITE)
            }

            btnLockDoor.setOnClickListener {
                doorLock.setValue(0)
            }
            btnOpenDoor.setOnClickListener {
                doorLock.setValue(1)
            }

            button.setPreventDoubleClick {
                val listElm = mutableMapOf<String, ElementInfoObj>()
                for (i in 1..4) {
                    val randomId = i.toString() + randomId()        //add index into first of id for sorting on database
                    listElm[randomId] = ElementInfoObj(randomId, "Nút $i")

                }
                deviceHelper.addNewDevice(
                    DeviceObj(
                        randomId(),
                        "test",
                        DeviceViewType.SWITCH_BUTTON.name,
                        mapOf()
                    ),
                    listElm
                )
            }
        }
    }

    private fun initFirebase() {
        val db = FirebaseDatabase.getInstance()
        nodeLed = db.reference.child("fan_remote/level1")
        nodeLed.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = (snapshot.value as Long).toInt()
                Log.d("duongnk", "led state = $value")
                binding.swLed.isChecked = value == 0
                binding.root.setBackgroundColor(if (value == 0) Color.BLUE else Color.WHITE)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("error_tag", error.message)
            }

        })

        nodeRelay1 = db.reference.child("$pathDB/switch/relay1")
        nodeRelay1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = (snapshot.value as Long).toInt()
                Log.d("duongnk", "switch 1 state = $value")
                binding.swRelay1.isChecked = value == 0
                binding.root.setBackgroundColor(if (value == 0) Color.CYAN else Color.WHITE)

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("error_tag", error.message)
            }

        })

        nodeRelay2 = db.reference.child("$pathDB/switch/relay2")
        nodeRelay2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = (snapshot.value as Long).toInt()
                Log.d("duongnk", "switch 2 state = $value")
                binding.swRelay2.isChecked = value == 0
                binding.root.setBackgroundColor(if (value == 0) Color.RED else Color.WHITE)

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("error_tag", error.message)
            }

        })

        relayEsp01 = db.reference.child("$pathDB/switch/relay_esp01")
        relayEsp01.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = (snapshot.value as Long).toInt()
                Log.d("duongnk", "switch esp 01 state = $value")
                binding.swRelayEsp01.isChecked = value == 0
                binding.root.setBackgroundColor(if (value == 0) Color.RED else Color.WHITE)

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("error_tag", error.message)
            }

        })

        doorLock = db.reference.child(("$pathDB/stepper/door_lock"))
        doorLock.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = (snapshot.value as Long).toInt()
                Log.d("duongnk", "switch esp 01 state = $value")
                if (value == 1) {
                    binding.apply {
                        btnOpenDoor.setBackgroundColor(Color.MAGENTA)
                        btnOpenDoor.isClickable = false
                        btnLockDoor.setBackgroundColor(Color.GRAY)
                        btnLockDoor.isClickable = true
                    }
                } else {
                    binding.apply {
                        btnLockDoor.setBackgroundColor(Color.MAGENTA)
                        btnLockDoor.isClickable = false
                        btnOpenDoor.setBackgroundColor(Color.GRAY)
                        btnOpenDoor.isClickable = true
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("error_tag", error.message)
            }

        })
    }

    companion object {
        fun newInstance() = ManageFragment()
    }

}