package ryuunta.iot.ryuuntaesp

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import ryuunta.iot.ryuuntaesp.authentication.AuthenticationHelper
import ryuunta.iot.ryuuntaesp.base.BaseFragment
import ryuunta.iot.ryuuntaesp.databinding.FragmentSplashScreenBinding
import ryuunta.iot.ryuuntaesp.utils.fadeIn

class SplashFragment : BaseFragment<FragmentSplashScreenBinding, InitiationViewModel>(
    FragmentSplashScreenBinding::inflate,
    InitiationViewModel::class.java
) {
    override fun initViews(view: View) {
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