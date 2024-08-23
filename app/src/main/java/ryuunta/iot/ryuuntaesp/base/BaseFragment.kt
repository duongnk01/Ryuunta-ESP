package ryuunta.iot.ryuuntaesp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import ryuunta.iot.ryuuntaesp.utils.RLog
import ryuunta.iot.ryuuntaesp.utils.hideKeyboard
import javax.inject.Inject


typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding, V : BaseViewModel>(
    private val inflate: Inflate<VB>,
    private val modelClasee: Class<V>
) : Fragment() {

    @Inject
    protected lateinit var viewModel: V

    private lateinit var _binding: VB
    val binding get() = _binding

    lateinit var navController: NavController

    private lateinit var alertDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        try {
            navController = findNavController()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        initViewModel(this)
        initViews(savedInstanceState)
        initEvents()
        if (isCustomBackPress()) {
            activity?.onBackPressedDispatcher?.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        customBackPress()
                    }

                })
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSetViewInfo()
    }

    open fun initViewModel(fragment: LifecycleOwner) {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory()
        )[modelClasee]

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
            RLog.d("Error", "error key: $key")
            RLog.d("Error", "error value: $value")
            if (value.isNullOrEmpty()) {
                handlerError(key, "")
            } else {
                handlerError(key, value)
            }
        }

        viewModel.loading.observe(fragment) {
            isScreenLoading(it)
        }
    }

    /**
     * Khởi tạo view
     */
    abstract fun initViews(savedInstanceState: Bundle?)
    protected open fun initEvents() {
        binding.root.setOnClickListener {
            it.hideKeyboard()
        }
    }

    protected open fun onSetViewInfo() {}

    /**
     * Xử lí response
     * @param tag: Keword phân biệt giữa các api
     * @param data: Dữ liệu trả về từ api, tự cast vs object tương ứng
     */
    protected open fun handlerResponse(tag: String, data: Any?) {}

    /**
     * Xử lý lỗi
     *  @param tag: Keword phân biệt giữa các api
     * @param message: Thông tin về lỗi
     */
    protected open fun handlerError(tag: String, message: String) {}

    open fun isScreenLoading(isLoading: Boolean) {
        if (isLoading)
            (activity as BaseActivity<*, *>).showLoadingScreen()
        else
            (activity as BaseActivity<*, *>).hideLoadingScreen()
    }

    open fun showAlertDialog(title: String?, message: String?) {
        alertDialog =
            AlertDialog.Builder(requireContext()).setTitle(title).setMessage(message).create()
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.show()
    }

    open fun hideAlertDialog() {
        if (alertDialog.isShowing) {
            alertDialog.dismiss()
        }
    }


    protected open fun isCustomBackPress() = false
    protected open fun customBackPress() {
        RLog.d("BaseFragment", "BaseFragment customBackPress")
    }

}