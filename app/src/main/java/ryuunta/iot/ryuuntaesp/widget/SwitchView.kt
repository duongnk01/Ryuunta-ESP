package ryuunta.iot.ryuuntaesp.widget

import android.animation.FloatEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import ryuunta.iot.ryuuntaesp.utils.convertDpToPixel

class SwitchView : View {
    interface SwitchViewListener {
        fun onSwitchChange(enable: Boolean)
    }

    private var widthSwitch = 0
    private var heightSwitch = 0
    private var pathSwitchBg: Path? = null
    private var paintSwitch: Paint? = null
    private var paintSwitchInside: Paint? = null
    private var paintSwitchBg: Paint? = null
    private var radiusSwitch = 0f
    private val tempCornerArcBounds = RectF()
    private var enable = false
    private var colorEnable = 0
    private var colorDisable = 0
    private var isPressing = false
    private var valueAnimator: ValueAnimator? = null
    private var cx = -1f
    var switchViewListener: SwitchViewListener? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        val widthSwitchDPI = 35f
        widthSwitch = context.convertDpToPixel(widthSwitchDPI).toInt()
        val heightSwitchDPI = 27f
        heightSwitch = context.convertDpToPixel(heightSwitchDPI).toInt()
        radiusSwitch = heightSwitch * 0.41f
        pathSwitchBg = Path()
        paintSwitchBg = Paint(Paint.ANTI_ALIAS_FLAG)
        paintSwitchBg!!.style = Paint.Style.FILL
        paintSwitchBg!!.color = Color.RED
        paintSwitch = Paint(Paint.ANTI_ALIAS_FLAG)
        paintSwitch!!.style = Paint.Style.FILL
        paintSwitch!!.color = Color.WHITE
        paintSwitchInside = Paint(Paint.ANTI_ALIAS_FLAG)
        paintSwitchInside!!.style = Paint.Style.FILL
        paintSwitchInside!!.color = Color.WHITE
        enable = false
        colorEnable = Color.parseColor("#78e9bd")
        colorDisable = Color.parseColor("#dddddd")
        valueAnimator = ValueAnimator()
        valueAnimator!!.duration = 300
        valueAnimator!!.setEvaluator(FloatEvaluator())
        valueAnimator!!.addUpdateListener { valueAnimator: ValueAnimator ->
            cx = valueAnimator.animatedValue as Float
            invalidate()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
            widthSwitch + paddingLeft + paddingRight,
            heightSwitch + paddingTop + paddingBottom
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val left = paddingLeft
        val top = paddingTop
        val right = left + widthSwitch
        val bottom = top + heightSwitch
        if (cx == -1f) cx =
            if (enable) (right - heightSwitch / 2).toFloat() else left + (heightSwitch / 2).toFloat()
        val cy = (top + heightSwitch / 2).toFloat()
        pathSwitchBg = Path()
        tempCornerArcBounds[left.toFloat(), top.toFloat(), (left + heightSwitch).toFloat()] =
            bottom.toFloat()
        pathSwitchBg!!.arcTo(
            tempCornerArcBounds,
            90.toFloat(), 180f
        )
        tempCornerArcBounds[(right - heightSwitch).toFloat(), top.toFloat(), right.toFloat()] =
            bottom.toFloat()
        pathSwitchBg!!.arcTo(
            tempCornerArcBounds,
            (-90).toFloat(), 180f
        )
        pathSwitchBg!!.close()
        paintSwitchBg!!.color = if (enable) colorEnable else colorDisable
        paintSwitchInside!!.color = if (enable) colorEnable else colorDisable
        canvas.drawPath(pathSwitchBg!!, paintSwitchBg!!)

//        paintSwitch.setShadowLayer(radiusSwitch/4,0,0, enable ? Color.parseColor("#999999") : Color.parseColor("#CCCCCC"));
        paintSwitch!!.color = if (isPressing) Color.parseColor("#EAEAEA") else Color.WHITE
        canvas.drawCircle(cx, cy, radiusSwitch, paintSwitch!!)
        //        canvas.drawCircle(cx,cy,radiusSwitch/3,paintSwitchInside);
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent): Boolean {
        //If the finger move to ouside
        val touchX = e.x
        val touchY = e.y
        when (e.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP -> {
                val left = paddingLeft
                val top = paddingTop
                val right = left + widthSwitch
                val bottom = top + heightSwitch
                if (touchX > left && touchX < right && touchY > top && touchY < bottom) {
                    if (e.action == MotionEvent.ACTION_DOWN) isPressing =
                        true else if (isPressing) {
                        enable = !enable
                        isPressing = false
                        startAnimation()
                        switchViewListener?.onSwitchChange(enable)
                    }

                    //Start animation here
                    return true
                }
                isPressing = false
                invalidate()
                return false
            }
            MotionEvent.ACTION_MOVE -> return true
        }
        return super.onTouchEvent(e)
    }

    private fun startAnimation() {
        valueAnimator!!.cancel()
        val left = paddingLeft
        val right = left + widthSwitch
        val r = (right - heightSwitch / 2).toFloat()
        val l = (left + heightSwitch / 2).toFloat()
        if (enable) valueAnimator!!.setFloatValues(l, r) else valueAnimator!!.setFloatValues(r, l)
        valueAnimator!!.start()
    }

    fun setEnable(enable: Boolean) {
        setEnable(enable, true)
    }

    fun setEnable(enable: Boolean, animation: Boolean) {
        if (this.enable != enable) {
            this.enable = enable
            if (animation)
                startAnimation()
        }
    }

    fun isEnable(): Boolean {
        return enable
    }
}