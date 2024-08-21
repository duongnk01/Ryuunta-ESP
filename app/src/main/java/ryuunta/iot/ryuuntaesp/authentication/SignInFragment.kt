package ryuunta.iot.ryuuntaesp.authentication

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionLayout.TransitionListener
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.base.BaseFragment
import ryuunta.iot.ryuuntaesp.databinding.FragmentSignInBinding
import ryuunta.iot.ryuuntaesp.main.MainActivity
import ryuunta.iot.ryuuntaesp.utils.RLog
import ryuunta.iot.ryuuntaesp.utils.fadeIn
import ryuunta.iot.ryuuntaesp.utils.isEmailAccountValid
import ryuunta.iot.ryuuntaesp.utils.isPasswordValid
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick
import ryuunta.iot.ryuuntaesp.utils.showDialogLottie
import ryuunta.iot.ryuuntaesp.utils.showDialogNotification
import ryuunta.iot.ryuuntaesp.utils.showDialogNotificationAutoDismiss
import ryuunta.iot.ryuuntaesp.utils.slideInBottom
import ryuunta.iot.ryuuntaesp.widget.StepView

class SignInFragment : BaseFragment<FragmentSignInBinding, AuthViewModel>(
    FragmentSignInBinding::inflate,
    AuthViewModel::class.java
) {

    private val stepView: StepView by lazy {
        StepView(requireContext())
    }

    override fun initViews(view: View) {
        binding.apply {
            fadeIn(container, 280)
            fadeIn(txtAppName, 280)
            slideInBottom(layoutSignIn)

            stepView.setStep(
                StepView.Step(layoutSignIn),
                StepView.Step(layoutSignUp)
            )
        }



    }

    override fun initEvents() {
        super.initEvents()

        binding.apply {
            motionSwip.setTransitionListener(object : TransitionListener {
                override fun onTransitionStarted(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int
                ) {

                }

                override fun onTransitionChange(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int,
                    progress: Float
                ) {


                }

                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                    if (currentId == R.id.end) {
                        val email = edtEmail.text.toString().trim()
                        val pass = edtInputPass.text.toString().trim()
                        RLog.d("email", email)
                        RLog.d("pass", pass)
                        RLog.d("pass length", pass.length.toString())
                        if (!isEmailAccountValid(email)) {
                            Toast.makeText(requireContext(),
                                getString(R.string.mail_form_not_corrected), Toast.LENGTH_SHORT)
                                .show()
                            motionSwip.transitionToStart()
                            return
                        }
                        if (email.isEmpty()) {
                            Toast.makeText(requireContext(),
                                getString(R.string.mail_is_empty), Toast.LENGTH_SHORT)
                                .show()
                            motionSwip.transitionToStart()
                            return
                        }
                        if (pass.isEmpty()) {
                            Toast.makeText(requireContext(),
                                getString(R.string.password_is_empty), Toast.LENGTH_SHORT)
                                .show()
                            motionSwip.transitionToStart()
                            return
                        }
                        if (!isPasswordValid(pass)) {
                            Toast.makeText(requireContext(),
                                getString(R.string.password_must_be_at_least_6_characters), Toast.LENGTH_SHORT)
                                .show()
                            motionSwip.transitionToStart()
                            return
                        }
                        viewModel.startEmailSignIn(edtEmail.text.toString(), edtInputPass.text.toString())

//                        motionSwip.isInteractionEnabled = false
                    }
                }

                override fun onTransitionTrigger(
                    motionLayout: MotionLayout?,
                    triggerId: Int,
                    positive: Boolean,
                    progress: Float
                ) {

                }

            })

            btnBack.setPreventDoubleClick {
                stepView.prevStep()
            }
            btnGoToSignUp.setPreventDoubleClick {
                stepView.nextStep()
            }
        }

        binding.btnSinInGoogle.setPreventDoubleClick {
            signInWithGoogle()
        }

        viewModel.authStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is AuthenticationState.Success -> {
                    RLog.d("AuthenticationState", "Success ${it.isSuccess}")
                    if (it.isSuccess) {
                        nextToHomePage()
                    }
                }

                is AuthenticationState.Failure -> {
                    RLog.d("AuthenticationState", "Failure: ${it.message}")
                    requireContext().showDialogNotification(
                        R.string.txt_failure,
                        R.raw.anim_nana_crying,
                        lifecycle,
                        R.string.txt_info_login_failure,
                        isAnimLoop = true
                    )
                    binding.motionSwip.transitionToStart()
                    viewModel.clearState()
                }

                else -> {}
            }
        }
    }

    private fun nextToHomePage() {
        requireContext().showDialogLottie(lifecycle, R.raw.anim_nana_wisher) {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }

    internal val signInGoogleForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.startGoogleSignIn(result.data)

            } else
                if (result.resultCode == Activity.RESULT_CANCELED)
                    viewModel.onFailure(result.resultCode, "Sign in with google was cancelled")

        }

}