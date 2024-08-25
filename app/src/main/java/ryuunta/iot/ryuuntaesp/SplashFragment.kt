package ryuunta.iot.ryuuntaesp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import ryuunta.iot.ryuuntaesp.core.helper.AuthenticationHelper
import ryuunta.iot.ryuuntaesp.core.base.BaseFragment
import ryuunta.iot.ryuuntaesp.databinding.FragmentSplashScreenBinding

class SplashFragment : BaseFragment<FragmentSplashScreenBinding, InitiationViewModel>(
    FragmentSplashScreenBinding::inflate,
    InitiationViewModel::class.java
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
        }, 2000)



    }

    private fun checkUser() {
        val currentUser = AuthenticationHelper.getInfoUser()
        if (currentUser != null) {
            (activity as InitiationActivity).nextToHomePage()
        } else {
            //remove splash screen from back stack
            navController.popBackStack(navController.graph.startDestinationId, true)
            //navigate to sign in fragment
            navController.navigate(R.id.signInFragment)
        }
    }
}