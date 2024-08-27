package ryuunta.iot.ryuuntaesp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.data.model.ElementInfoObj

class ButtonElementView constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

//    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
//        setAttrs(context, attrs)
//    }
//    constructor(context: Context) : super(context, null)

    private var btnButton: LinearLayout
    private var imgPower: ImageView
    private var tvLabel: TextView

    private var _id: String = ""
    private var _label: String = ""
    private var _state = false

    val id: String
        get() = _id

    var label: String
        get() = _label
        set(value) {
            _label = value
            tvLabel.text = value
        }

    var isOn: Boolean
        get() = _state
        set(value) {
            _state = value
            imgPower.setImageResource(
                if (value) R.drawable.ic_power_switch_on
                else R.drawable.ic_power_switch_off
            )
        }

    init {
        setAttrs(context, attrs)
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_button_element, this, true)

        btnButton = findViewById(R.id.btn_button)
        imgPower = findViewById(R.id.img_power)
        tvLabel = findViewById(R.id.tv_label)

    }

    private fun setAttrs(context: Context, attrs: AttributeSet?) {
        try {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ButtonElementView)
            _label = typedArray.getString(R.styleable.ButtonElementView_elmLabel) ?: ""
            _state = typedArray.getBoolean(R.styleable.ButtonElementView_isPowerOn, false)

            typedArray.recycle()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun initView(elm: ElementInfoObj, onButtonClick: (Boolean) -> Unit) {
        _label = elm.label
        _id = elm.id
        tvLabel.text = label
//        imgPower.setImageResource(if (_state) R.drawable.ic_power_switch_on else R.drawable.ic_power_switch_off)

        btnButton.setOnClickListener {
            _state = !_state
//            imgPower.setImageResource(if (_state) R.drawable.ic_power_switch_on else R.drawable.ic_power_switch_off)
            onButtonClick(_state)
        }

    }
}