package ryuunta.iot.ryuuntaesp.widget

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import ryuunta.iot.ryuuntaesp.utils.slideToNextView
import ryuunta.iot.ryuuntaesp.utils.slideToPrevView

class StepView(
    private val context: Context
) {
    data class Step(
        val view: View,
    )

    private var steps: MutableList<Step> = mutableListOf()
    private var position = 0
    private var onNextStep: (nextId: Int) -> Unit = {}
    private var onPreStep: (id: Int) -> Unit = {}
    private var onStepChange: (id: Int) -> Unit = {}

    val currentStep: Int
        @IdRes
        get() = steps[position].view.id


    fun setStep(vararg steps: Step): StepView {
        this.steps.clear()
        this.steps.addAll(steps)
        steps.forEach { it.view.visibility = View.GONE }
        position = 0
        steps[position].view.visibility = View.VISIBLE
        return this
    }

    fun setStep(steps: List<Step>): StepView {
        this.steps.clear()
        this.steps.addAll(steps)
        steps.forEach { it.view.visibility = View.GONE }
        position = 0
        steps[position].view.visibility = View.VISIBLE
        return this
    }

    fun onNextStep(func: (id: Int) -> Unit): StepView {
        this.onNextStep = func
        return this
    }

    fun onPrevStep(func: (id: Int) -> Unit): StepView {
        this.onPreStep = func
        return this
    }

    fun onStepChange(func: (id: Int) -> Unit): StepView {
        this.onStepChange = func
        return this
    }

    fun resetStep(): Boolean {
        if (position > 0) {
            slideToNextView(steps[position].view, steps[0].view)
            position = 0
            onPreStep(steps[position].view.id)
            onStepChange(steps[position].view.id)
            return true
        }
        return false
    }

    fun nextTo(position: Int) {
        if (position > steps.size || position < 0 || this.position == position) return
        slideToNextView(steps[this.position].view, steps[position].view)
        onNextStep(steps[position].view.id)
        onStepChange(steps[position].view.id)
        this.position = position
    }

    fun prevTo(position: Int) {
        if (position > steps.size || position < 0 || this.position == position) return
        slideToPrevView(steps[this.position].view, steps[position].view)
        onPreStep(steps[position].view.id)
        onStepChange(steps[position].view.id)
        this.position = position
    }

    fun toStep(viewId: Int) {
        val pos = steps.indexOfFirst { it.view.id == viewId }
        if (pos > position) {
            nextTo(pos)
            return
        }
        prevTo(pos)

    }

    fun initStep(position: Int = 0) {
        steps.forEach { it.view.visibility = View.GONE }
        steps[position].view.visibility = View.VISIBLE
        onStepChange(steps[position].view.id)
        this.position = position
    }

    fun nextStep() {
        if (position < steps.size - 1) {
            slideToNextView(steps[position].view, steps[++position].view)
            onNextStep(steps[position].view.id)
            onStepChange(steps[position].view.id)
            return
        }

        slideToPrevView(steps[position].view, steps[0].view)
        position = 0
        onNextStep(steps[0].view.id)
        onStepChange(steps[position].view.id)
    }

    fun prevStep(): Boolean {
        if (position > 0) {
            slideToPrevView(steps[position].view, steps[--position].view)
            onPreStep(steps[position].view.id)
            onStepChange(steps[position].view.id)
            return true
        }
        return false
    }

    fun insertAt(view: View, position: Int) {
        steps.add(position, Step(view))
    }

    fun getIdAt(position: Int) = steps[position].view.id

    fun removeAt(position: Int, view: View) {
        if (steps[position].view.id == view.id)
            steps.removeAt(position)
    }

    fun show() {
        steps[position].view.visibility = View.VISIBLE
    }

    fun gone() {
        steps[position].view.visibility = View.GONE
    }

}