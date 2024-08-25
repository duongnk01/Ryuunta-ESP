package ryuunta.iot.ryuuntaesp.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionLayout.TransitionListener
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.core.base.BaseFragment
import ryuunta.iot.ryuuntaesp.common.DialogLottie
import ryuunta.iot.ryuuntaesp.databinding.FragmentSignInBinding
import ryuunta.iot.ryuuntaesp.RMainActivity
import ryuunta.iot.ryuuntaesp.core.state.AuthenticationState
import ryuunta.iot.ryuuntaesp.utils.RLog
import ryuunta.iot.ryuuntaesp.utils.fadeIn
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick
import ryuunta.iot.ryuuntaesp.utils.showDialogNegative
import ryuunta.iot.ryuuntaesp.utils.showDialogNotification
import ryuunta.iot.ryuuntaesp.utils.slideInBottom
import ryuunta.iot.ryuuntaesp.widget.StepView

class SignInFragment : BaseFragment<FragmentSignInBinding, AuthViewModel>(
    FragmentSignInBinding::inflate,
    AuthViewModel::class.java
) {

    private val stepView: StepView by lazy {
        StepView(requireContext())
    }

    private val dialogLottie: DialogLottie by lazy {
        DialogLottie(requireContext())
    }

    override fun initViews(view: Bundle?) {
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
                        if (authValidation(email, pass)) {
                            viewModel.startEmailSignIn(email, pass)
                            if (!dialogLottie.isShowing)
                                dialogLottie.show(lifecycle, R.raw.anim_nana_wisher)
                        } else {
                            motionSwip.transitionToStart()
                        }

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
            btnSignUp.setPreventDoubleClick {
                val email = edtSignUpEmail.text.toString().trim()
                val pass = edtSignUpPass.text.toString().trim()
                if (authValidation(email, pass))
                    viewModel.startEmailSignUp(email, pass)
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
                        dialogLottie.onDismissWhenAnimationDone {
                            nextToHomePage()
                        }
                    }
                }

                is AuthenticationState.SignUpSuccess -> {
                    RLog.d("AuthenticationState", "Sign up Success ${it.email}")
                    requireContext().showDialogNegative(
                        R.string.txt_done,
                        R.string.txt_create_account_success,
                        lottieAnim = R.raw.anim_paimon_like,
                        lifecycle = lifecycle,
                        cancelRes = R.string.txt_no,
                        confirmRes = R.string.txt_ok_now,
                        onConfirm = {
                            if (!dialogLottie.isShowing)
                                dialogLottie.show(lifecycle, R.raw.anim_nana_wisher)
                            viewModel.startEmailSignIn(it.email, it.password)
                        }
                    )
                }

                is AuthenticationState.Failure -> {
                    RLog.d("AuthenticationState", "Failure: ${it.message}")
                    if (dialogLottie.isShowing) {
                        dialogLottie.onDismissWhenAnimationDone {
                            requireContext().showDialogNotification(
                                R.string.txt_failure,
                                R.raw.anim_nana_crying,
                                lifecycle,
                                R.string.txt_info_login_failure,
                                errorMess = it.message,
                                isAnimLoop = true
                            )
                            binding.motionSwip.transitionToStart()
                        }
                    } else {
                        requireContext().showDialogNotification(
                            R.string.txt_failure,
                            R.raw.anim_nana_crying,
                            lifecycle,
                            R.string.txt_info_login_failure,
                            errorMess = it.message,
                            isAnimLoop = true
                        )
                    }
                    viewModel.clearState()
                }

                else -> {}
            }

        }
    }

    private fun nextToHomePage() {
        val intent = Intent(requireActivity(), RMainActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

    }

    internal val signInGoogleForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (!dialogLottie.isShowing)
                    dialogLottie.show(lifecycle, R.raw.anim_nana_wisher)

                viewModel.startGoogleSignIn(result.data)

            } else
                if (result.resultCode == Activity.RESULT_CANCELED)
                    viewModel.onFailure(result.resultCode, "Sign in with google was cancelled")

        }

}