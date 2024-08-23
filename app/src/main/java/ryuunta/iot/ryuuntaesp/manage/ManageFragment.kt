package ryuunta.iot.ryuuntaesp.manage

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ryuunta.iot.ryuuntaesp.base.BaseFragment
import ryuunta.iot.ryuuntaesp.databinding.FragmentManageBinding
import ryuunta.iot.ryuuntaesp.MainActivity

class ManageFragment : BaseFragment<FragmentManageBinding, ManageViewModel>(FragmentManageBinding::inflate, ManageViewModel::class.java) {

    private val pathDB = "esp8266"
    private lateinit var nodeLed: DatabaseReference
    private lateinit var nodeRelay1: DatabaseReference
    private lateinit var nodeRelay2: DatabaseReference
    private lateinit var relayEsp01: DatabaseReference
    private lateinit var doorLock: DatabaseReference

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).headerHome(false)
    }

    override fun initViews(view: Bundle?) {
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
        }
    }

    private fun initFirebase() {
        val db = FirebaseDatabase.getInstance()
        nodeLed = db.reference.child("$pathDB/digital/led")
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