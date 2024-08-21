package ryuunta.iot.ryuuntaesp.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import ryuunta.iot.ryuuntaesp.base.wrapper.AuthRequestCallbackWrapper
import ryuunta.iot.ryuuntaesp.utils.RLog

object AuthenticationHelper {
    private val TAG = "AuthenticationHelper"

    private val firebaseAuth : FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun signIn(
        method: SignInMethod,
        onSuccess:() -> Unit = {},
        onFailure: (code: Int, message: String) -> Unit = {_, _ -> }
    ) {
        RLog.d(TAG, "start sign in")
        when(method) {
            is SignInMethod.SignInGoogle -> {
                firebaseAuth.signInWithCredential(method.credential)
                    .addOnSuccessListener {
                       onSuccess()
                    }
                    .addOnFailureListener {
                        onFailure(-1, "Sign In Google failure")
                    }
                    .addOnCanceledListener {
                        onFailure(-2, "Sign In Google canceled")
                    }
            }
            is SignInMethod.SignInEmail -> {
                firebaseAuth.signInWithEmailAndPassword(method.email, method.password)
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener { task ->
                        onFailure(-1, "Sign In email failure")
                        task.printStackTrace()
                    }
                    .addOnCanceledListener {
                        onFailure(-2, "Sign In email canceled")
                    }
            }
        }
    }

    fun signOut(isSuccess: () -> Unit) {
        firebaseAuth.signOut()
        FirebaseMessaging.getInstance().deleteToken()
        isSuccess()
    }
}