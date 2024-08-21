package ryuunta.iot.ryuuntaesp.authentication

import com.google.firebase.auth.AuthCredential

sealed class SignInMethod {

    class SignInGoogle(var credential: AuthCredential) : SignInMethod()

    class SignInEmail(var email:String, var password: String) : SignInMethod()
}