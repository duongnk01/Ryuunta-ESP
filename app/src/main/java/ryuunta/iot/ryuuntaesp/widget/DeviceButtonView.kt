package ryuunta.iot.ryuuntaesp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import com.google.gson.Gson
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.data.model.DeviceObj
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
        inflater.inflate(R.layout.device_button_view, this, true)

        layout1 = findViewById(R.id.layout_1)
        layout2 = findViewById(R.id.layout_2)
        layout3 = findViewById(R.id.layout_3)
    }

    fun initView(deviceItem: DeviceObj, onElementClick: (String, Boolean) -> Unit) {
        when (deviceItem.type) {
            DeviceViewType.SWITCH_BUTTON -> {
                val splitMap = splitHashMap(deviceItem.buttonList, 2)
                val gson = Gson().toJson(splitMap)
                RLog.d(TAG, "initView: $gson")

                splitMap.forEachIndexed { index, map ->
                    for ((key, value) in map) {
                        val button = ButtonElementView(context)
                        button.initView(value) { state ->
                            onElementClick(value, state)
                        }
                        button.label = value
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

            DeviceViewType.FAN_REMOTE -> {

            }

            else -> {}
        }
    }

    fun updateView(elm: String, state: Boolean) {
        try {
            val button = listElement.single { it.label == elm }
            RLog.d(TAG, "updateView: button = ${button.label}, state = $state")
            button.isOn = state
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                context,
                context.getString(R.string.error_element_not_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

