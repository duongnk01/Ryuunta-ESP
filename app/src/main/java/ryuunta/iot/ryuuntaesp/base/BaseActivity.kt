package ryuunta.iot.ryuuntaesp.base

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.viewbinding.ViewBinding
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.common.DialogLoading
import ryuunta.iot.ryuuntaesp.utils.RLog
import javax.inject.Inject
import kotlin.system.exitProcess

abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel>(
    private val modelClass: Class<VM>
) : AppCompatActivity() {

    private val TAG = this.javaClass.simpleName

    @Inject
    lateinit var viewModel: VM

    @Inject
    protected lateinit var binding: VB

    @LayoutRes
    abstract fun getLayoutRes(): Int

    lateinit var navController: NavController

    var alertDialog: AlertDialog? = null
    var doubleBackToExitPressedOnce = false

    private val dialogLoading: DialogLoading by lazy {
        DialogLoading(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val darkModeFlag = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (darkModeFlag == Configuration.UI_MODE_NIGHT_NO) {
            setTheme(R.style.Theme_RyuuntaESP)
        } else {
            setTheme(R.style.Theme_RyuuntaESP_Dark)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initViewModel(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, getLayoutRes()) as VB
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViews()
        initEvents()
        RLog.d(TAG, "oncreate baseactivity")
        if (savedInstanceState != null) {
            try {
                RLog.d(TAG, "savedInstanceState != null")
                val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
                startActivity(launchIntent)
                finish()
                System.gc()
                exitProcess(0)

            } catch (e: Exception) {
                RLog.e(TAG, "savedInstanceState != null")
                e.printStackTrace()
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                customBackPressed()
            }

        })
    }

    abstract fun initViews()

    abstract fun initEvents()

    open fun initViewModel(fragment: LifecycleOwner) {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory()
        )[modelClass]

        viewModel.baseResult.observe(fragment) {
            val key = it.keys.elementAt(0)
            val value = it[key]
            RLog.d("Data result", "success key: $key")
            RLog.d("Data result", "success value: $value")
            handlerResponse(key, value)
        }

        viewModel.errorMessage.observe(fragment) {
            val key = it.keys.elementAt(0)
            val value = it[key]
            RLog.e("Error", "error key: $key")
            RLog.e("Error", "error value: $value")
            if (value.isNullOrEmpty()) {
                handlerError(key, "")
            } else {
                handlerError(key, value)
            }
        }

        viewModel.loading.observe(fragment) {
            if (it) {
                showLoadingScreen()
            } else {
                hideLoadingScreen()
            }
        }
    }

    /**
     * Xử lí response
     * @param tag: Keword phân biệt giữa các api
     * @param data: Dữ liệu trả về từ api, tự cast vs object tương ứng
     */
    open fun handlerResponse(tag: String, data: Any?) {}

    /**
     * Xử lý lỗi
     *  @param tag: Keword phân biệt giữa các api
     * @param message: Thông tin về lỗi
     */
    open fun handlerError(tag: String, message: String) {}


    fun showAlertDialog(title: String?, message: String?) {
        if (alertDialog == null) alertDialog =
            AlertDialog.Builder(this).setTitle(title).setMessage(message).create() else {
            alertDialog!!.setTitle(title)
            alertDialog!!.setMessage(message)
        }
        alertDialog!!.show()
    }

    fun hideAlertDialog() {
        if (alertDialog != null && alertDialog!!.isShowing) {
            alertDialog!!.dismiss()
        }
    }

    /**
     * Show Loading Screen
     */
    //    LoadingScreen loadingScreen;
    var loadingDialog: DialogLoading? = null

    fun showLoadingScreen() {
        dialogLoading.show(
            lifecycle,
            R.string.loading,
            R.string.wait_a_few
        )
    }

    fun hideLoadingScreen() {
        if (dialogLoading.isShowing) {
            dialogLoading.onDismissAndRemoveRes()
        }
    }

    protected open fun customBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStackImmediate()
        } else {
            if (doubleBackToExitPressedOnce) {
                finish()
                return
            }

            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

//            Snackbar.make(
//                this,
//                "Please click BACK again to exit",
//                Snackbar.LENGTH_SHORT
//            ).show()

            Handler(Looper.getMainLooper()).postDelayed(
                { doubleBackToExitPressedOnce = false },
                2000
            )
        }
    }


}