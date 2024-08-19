package ryuunta.iot.ryuuntaesp.authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import ryuunta.iot.ryuuntaesp.base.BaseFragment
import ryuunta.iot.ryuuntaesp.databinding.FragmentSignInBinding
import ryuunta.iot.ryuuntaesp.main.MainActivity
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick

class SignInFragment : BaseFragment<FragmentSignInBinding, AuthViewModel>(FragmentSignInBinding::inflate, AuthViewModel::class.java) {

    private val RC_SIGN_IN = 9001
    protected lateinit var googleSignInClient: GoogleSignInClient

    override fun initViews(view: View) {
        val googleSignOpt =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignOpt)

    }

    override fun initEvents() {
        binding.btnSinInGoogle.setPreventDoubleClick {

            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
            signInWithGoogle()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            val bundle = Bundle()
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            bundle.putString("accountName", account.displayName)
            intent.putExtras(bundle)
            startActivity(intent)
        } catch (e: ApiException) {
            e.printStackTrace()
        }
    }

}