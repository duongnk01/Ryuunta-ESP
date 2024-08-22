package ryuunta.iot.ryuuntaesp.authentication

import android.widget.Toast
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.utils.isEmailAccountValid
import ryuunta.iot.ryuuntaesp.utils.isPasswordValid

fun SignInFragment.signInWithGoogle() {
    val googleSingInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSingInOptions)

    //call this function to make sure logged out before signing in
    googleSignInClient.signOut()

    val intent = googleSignInClient.signInIntent
    signInGoogleForResult.launch(intent)
}

fun SignInFragment.authValidation(email: String, pass: String) : Boolean {
    if (email.isEmpty()) {
        Toast.makeText(requireContext(),
            getString(R.string.mail_is_empty), Toast.LENGTH_SHORT)
            .show()
        return false
    }
    if (!isEmailAccountValid(email)) {
        Toast.makeText(requireContext(),
            getString(R.string.mail_form_not_corrected), Toast.LENGTH_SHORT)
            .show()
        return false
    }
    if (pass.isEmpty()) {
        Toast.makeText(requireContext(),
            getString(R.string.password_is_empty), Toast.LENGTH_SHORT)
            .show()
        return false
    }
    if (!isPasswordValid(pass)) {
        Toast.makeText(requireContext(),
            getString(R.string.password_must_be_at_least_6_characters), Toast.LENGTH_SHORT)
            .show()
        return false
    }
    return true
}
