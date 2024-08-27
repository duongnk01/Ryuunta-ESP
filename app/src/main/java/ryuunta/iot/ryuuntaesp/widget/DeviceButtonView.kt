package ryuunta.iot.ryuuntaesp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import com.google.gson.Gson
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.data.model.DeviceObj
import ryuunta.iot.ryuuntaesp.data.model.ElementInfoObj
import ryuunta.iot.ryuuntaesp.main.home.devices.DeviceViewType
import ryuunta.iot.ryuuntaesp.utils.RLog
import ryuunta.iot.ryuuntaesp.utils.show
import ryuunta.iot.ryuuntaesp.utils.splitHashMap

class DeviceButtonView : LinearLayout {

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context) : super(context, null)

    internal val TAG = "DeviceButtonView"

    private var layout1: LinearLayout
    private var layout2: LinearLayout
    private var layout3: LinearLayout

    private val listElement = mutableListOf<ButtonElementView>()

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_device_button, this, true)

        layout1 = findViewById(R.id.layout_1)
        layout2 = findViewById(R.id.layout_2)
        layout3 = findViewById(R.id.layout_3)
    }

    fun initView(deviceItem: DeviceObj, onElementClick: (ElementInfoObj, Boolean) -> Unit) {
        when (deviceItem.type) {
            DeviceViewType.SWITCH_BUTTON.name, DeviceViewType.FAN_REMOTE.name -> {
                val splitList = deviceItem.buttonList.chunked(2)
                val gson = Gson().toJson(splitList)
                RLog.d(TAG, "initView: $gson")

                splitList.forEachIndexed { index, list ->
                    for (item in list) {
                        val button = ButtonElementView(context)
                        button.initView(item) { state ->
                            onElementClick(item, state)
                        }
                        listElement.add(button)
                        when (index) {
                            0 -> layout1.addView(button)
                            1 -> {
                                layout2.show()
                                layout2.addView(button)
                            }

                            2 -> {
                                layout3.show()
                                layout3.addView(button)
                            }

                            else -> {}
                        }
                    }
                }
            }

            DeviceViewType.FAN_REMOTE.name -> {

            }

            else -> {}
        }
    }

    fun updateView(elm: ElementInfoObj) {
        try {
            val button = listElement.single { it.id == elm.id }
            RLog.d(TAG, "updateView: button = ${button.label}, state = ${elm.value}")
            button.isOn = elm.value == 1
            button.label = elm.label
        } catch (e: Exception) {
            e.printStackTrace()
//            Toast.makeText(
//                context,
//                context.getString(R.string.error_element_not_found),
//                Toast.LENGTH_SHORT
//            ).show()
        }
    }
}

