package ryuunta.iot.ryuuntaesp.authentication

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ryuunta.iot.ryuuntaesp.base.BaseViewModel
import ryuunta.iot.ryuuntaesp.data.network.RetrofitService

class AuthViewModel : BaseViewModel() {

    private var _authStateLiveData: MutableLiveData<AuthenticationState?> = MutableLiveData()
    val authStateLiveData: LiveData<AuthenticationState?> = _authStateLiveData


    fun onSuccess(isSuccess: Boolean) {
        loading.postValue(false)
        _authStateLiveData.value = AuthenticationState.Success(isSuccess)

    }

    fun onFailure(code: Int, message: String?) {
        loading.postValue(false)
        viewModelScope.launch(Dispatchers.Main) {
            _authStateLiveData.value = AuthenticationState.Failure(code, message)
        }
    }

    fun clearState() {
        _authStateLiveData.value = null
    }


    fun startGoogleSignIn(data: Intent?) {
        loading.postValue(true)
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
            AuthenticationHelper.signIn(
                SignInMethod.SignInGoogle(credential),
                onSuccess = {
                    onSuccess(true)
                },
                onFailure = { code, message ->
                    onFailure(code, message)
                })
        } catch (e: Exception) {
            onFailure(2, "Sign in with google failure")
        }
    }

    fun startEmailSignIn(email: String, password: String) {
        loading.postValue(true)
        AuthenticationHelper.signIn(
            SignInMethod.SignInEmail(email, password),
            onSuccess = {
                onSuccess(true)
            },
            onFailure = { code, message ->
                onFailure(code, message)
            })
    }
}