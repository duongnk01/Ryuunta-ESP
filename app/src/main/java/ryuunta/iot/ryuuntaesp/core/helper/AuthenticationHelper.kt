package ryuunta.iot.ryuuntaesp.core.helper

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import ryuunta.iot.ryuuntaesp.authentication.SignInMethod
import ryuunta.iot.ryuuntaesp.utils.RLog

object AuthenticationHelper {
    private val TAG = "AuthenticationHelper"

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun getInfoUser(): FirebaseUser? {
        val user = firebaseAuth.currentUser
        RLog.d(TAG, "User name: ${user?.displayName}")
        RLog.d(TAG, "mail: ${user?.email}")
        RLog.d(TAG, "avatar url: ${user?.photoUrl}")
        RLog.d(TAG, "providerID: ${user?.providerId}")
        return user
    }

    fun signUp(
        email: String,
        password: String,
        onSuccess: () -> Unit = {},
        onFailure: (code: Int, message: String?) -> Unit = { _, _ -> }
    ) {
        RLog.d(TAG, "start sign up")
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { task ->
                onFailure(-1, task.localizedMessage)
                task.printStackTrace()
            }
            .addOnCanceledListener {
                onFailure(-2, "Sign Up canceled")
            }
    }

    fun signIn(
        method: SignInMethod,
        onSuccess: () -> Unit = {},
        onFailure: (code: Int, message: String?) -> Unit = { _, _ -> }
    ) {
        RLog.d(TAG, "start sign in")
        when (method) {
            is SignInMethod.SignInGoogle -> {
                firebaseAuth.signInWithCredential(method.credential)
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener { task ->
                        onFailure(-1, task.localizedMessage)
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
                        onFailure(-1, task.localizedMessage)
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