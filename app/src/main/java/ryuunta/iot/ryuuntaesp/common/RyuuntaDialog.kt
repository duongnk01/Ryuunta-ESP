package ryuunta.iot.ryuuntaesp.common

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ContextMenu
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.base.BaseDialog

abstract class RyuuntaDialog<T : ViewDataBinding>(
    protected val context: Context,
    @LayoutRes private val lid: Int = -1
) {
    private var lifecycle: Lifecycle? = null
    var isShowing: Boolean = false
    var dialog: BaseDialog = BaseDialog(context)

    private val observer = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_PAUSE -> {

            }

            Lifecycle.Event.ON_STOP -> {
//                onDismissAndRemoveRes()
//                isShowing = false
            }

            Lifecycle.Event.ON_DESTROY -> {
                onDismissAndRemoveRes()
                isShowing = false
            }

            Lifecycle.Event.ON_RESUME -> {
//                if (!isShowing) {
//                    dialog.show()
//                    isShowing = true
//                }
                onResume()
            }

            else -> {

            }
        }
    }
    protected var bindingComponent: DataBindingComponent? = DataBindingUtil.getDefaultComponent()
    protected val contentView: View

    /** A backing field for providing an immutable [binding] property.  */
    private var _binding: T? = null
    private var isCleanRes = true

    init {
        contentView =
            LayoutInflater.from(context).inflate(if (lid == -1) this.getLayoutId() else lid, null)

        dialog.setContentView(contentView)
        dialog.setCancelable(this.isCancellable())
        dialog.setCanceledOnTouchOutside(this.isCanceledOnTouchOutside())


        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(this.dialogGravity())
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            if (this.isFullScreen()) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.attributes?.windowAnimations = R.style.RogoDialogAnimation

        dialog.onCreateContextMenu { menu, v, menuInfo ->
            onCreateContextMenu(menu, v, menuInfo)
        }

        dialog.onContextItemSelected {
            onContextItemSelected(it)
        }
        dialog.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
                if (this.isCancellable()) {
                    onDismissAndRemoveRes()
                    return@setOnKeyListener true
                }
                onBackStack()
                return@setOnKeyListener true
            }
            false
        }
        _binding = this.initBinding()
        dialog.setOnShowListener {
            onDialogShown()
        }
        this.onCreate()

        dialog.setOnDismissListener {
            onDismiss()
        }
    }

    open fun dialogGravity() = Gravity.BOTTOM

    /**
     * A data-binding property will be initialized in.
     * And provide the inflated view which depends on.
     */
    val binding: T
        get() = checkNotNull(_binding) {
            "Fragment $this binding cannot be accessed before onCreateView() or after onDestroyView()"
        }

    inline fun binding(block: T.() -> Unit): T {
        return binding.apply(block)
    }

    protected var isInit = false
    protected open fun onDialogShown() {
        if (!isInit) {
            initControl()
            isInit = true
        }
    }

    protected open fun initControl() {}

    protected open fun onBackStack() {}

    protected abstract fun onCreate()
    protected open fun getLayoutId(): Int = -1
    protected abstract fun initBinding(): T

    open fun isCancellable() = true
    open fun isCanceledOnTouchOutside(): Boolean {
        isCleanRes = true
        return false
    }

    open fun isFullScreen() = false

    fun setLifecycle(lifecycle: Lifecycle): RyuuntaDialog<T> {
        this.lifecycle = lifecycle
        lifecycle.addObserver(observer)
        return this
    }

    open fun onDismissAndRemoveRes() {
        dialog.dismiss()
//        lifecycle?.removeObserver(observer)
        isShowing = false
        isCleanRes = true
    }

    open fun onDismissAndNotRemoveRes() {
        dialog.dismiss()
        isCleanRes = false
        isShowing = false
    }

    open fun show(lifecycle: Lifecycle) {
        this.lifecycle?.removeObserver(observer)
        this.lifecycle = lifecycle
        if (!dialog.isShowing) {
            onDismissAndRemoveRes()
            lifecycle.addObserver(observer)
            dialog.show()
            isShowing = true
            isCleanRes = true
        }
    }

    fun registerForContextMenu(view: View) {
        dialog.registerForContextMenu(view)
    }

    open fun onDismiss() {
        if (isCleanRes)
            lifecycle?.removeObserver(observer)
    }

    open fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {

    }

    open fun onResume() {}

    open fun onContextItemSelected(item: MenuItem): Boolean = false


    fun getString(@StringRes resId: Int): String = context.getString(resId)
}