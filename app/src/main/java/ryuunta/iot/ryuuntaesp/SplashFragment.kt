package ryuunta.iot.ryuuntaesp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import ryuunta.iot.ryuuntaesp.authentication.AuthViewModel
import ryuunta.iot.ryuuntaesp.core.base.BaseFragment
import ryuunta.iot.ryuuntaesp.data.model.UserInfo
import ryuunta.iot.ryuuntaesp.databinding.FragmentSplashScreenBinding
import ryuunta.iot.ryuuntaesp.helper.AuthenticationHelper
import ryuunta.iot.ryuuntaesp.helper.ControlHelper
import ryuunta.iot.ryuuntaesp.utils.showDialogNotification

class SplashFragment : BaseFragment<FragmentSplashScreenBinding, AuthViewModel>(
    FragmentSplashScreenBinding::inflate,
    AuthViewModel::class.java
) {
    override fun initViews(savedInstanceState: Bundle?) {
        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            requireActivity().window.insetsController?.hide(WindowInsets.Type.statusBars())
//        } else {
//            requireActivity().window.setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN
//            )
//        }

        Handler(Looper.getMainLooper()).postDelayed({
            checkUser()
        }, 1000)

    }

    private fun checkUser() {
        val currentUser = AuthenticationHelper.getInfoUser()
        if (currentUser != null) {
            val userInfo = UserInfo(
                currentUser.displayName ?: "",
                currentUser.email ?: ""
            )
            ControlHelper().generateUserData(currentUser.uid, userInfo,
                onSuccess = {
                    (activity as InitiationActivity).nextToHomePage()
                },
                onFailure = { code, message ->
                    requireContext().showDialogNotification(
                        R.string.txt_failure,
                        R.raw.anim_nana_crying,
                        lifecycle,
                        R.string.txt_cannot_get_data_user,
                        errorMess = message,
                        onConfirm = {
                            goToSignIn()
                        }
                    )

                })

        } else {
            goToSignIn()

        }
    }

    private fun goToSignIn() {
        //remove splash screen from back stack
        navController.popBackStack(navController.graph.startDestinationId, true)
        //navigate to sign in fragment
        navController.navigate(R.id.signInFragment)
    }
}