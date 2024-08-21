package ryuunta.iot.ryuuntaesp.authentication

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import ryuunta.iot.ryuuntaesp.R

fun SignInFragment.signInWithGoogle() {
    val googleSingInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSingInOptions)
    val intent = googleSignInClient.signInIntent
    signInGoogleForResult.launch(intent)
//    startActivityForResult(intent, RC_SIGN_IN)
}
