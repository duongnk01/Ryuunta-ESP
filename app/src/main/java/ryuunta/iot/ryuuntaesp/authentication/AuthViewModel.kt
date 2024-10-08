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
import ryuunta.iot.ryuuntaesp.core.base.BaseViewModel
import ryuunta.iot.ryuuntaesp.core.base.Config
import ryuunta.iot.ryuuntaesp.helper.AuthenticationHelper
import ryuunta.iot.ryuuntaesp.core.state.AuthenticationState
import ryuunta.iot.ryuuntaesp.data.model.HouseObj
import ryuunta.iot.ryuuntaesp.data.model.UserInfo
import ryuunta.iot.ryuuntaesp.data.network.ResponseCode
import ryuunta.iot.ryuuntaesp.helper.ControlHelper
import ryuunta.iot.ryuuntaesp.helper.GroupHelper
import ryuunta.iot.ryuuntaesp.utils.randomId

class AuthViewModel : BaseViewModel() {

    private var _authStateLiveData: MutableLiveData<AuthenticationState?> = MutableLiveData()
    val authStateLiveData: LiveData<AuthenticationState?> = _authStateLiveData


    fun onSuccess(isSuccess: Boolean) {
        _authStateLiveData.value = AuthenticationState.Success(isSuccess)

    }

    fun onFailure(code: Int, message: String?) {
        viewModelScope.launch(Dispatchers.Main) {
            _authStateLiveData.value = AuthenticationState.Failure(code, message)
        }
    }

    fun clearState() {
        _authStateLiveData.value = null
    }


    fun startGoogleSignIn(data: Intent?) {
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
        AuthenticationHelper.signIn(
            SignInMethod.SignInEmail(email, password),
            onSuccess = {
                onSuccess(true)
            },
            onFailure = { code, message ->
                onFailure(code, message)
            })
    }

    fun startEmailSignUp(email: String, password: String) {
        loading.postValue(true)
        AuthenticationHelper.signUp(
            email, password,
            onSuccess = {
                loading.postValue(false)
                _authStateLiveData.value = AuthenticationState.SignUpSuccess(email, password)
            },
            onFailure = { code, message ->
                loading.postValue(false)
                onFailure(code, message)
            })
    }

    fun fetchDataUser() : LiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()
        val user = AuthenticationHelper.getInfoUser()
        user?.let {
            val userInfo = UserInfo(
                it.displayName ?: "",
                it.email ?: ""
            )
            ControlHelper().generateUserData(it.uid, userInfo,
                onSuccess = {
                    GroupHelper().getHouseList { houses ->
                        if (houses.isEmpty()) {     //auto generate a house if not exist
                            GroupHelper().createHouse(
                                HouseObj(
                                    randomId(),
                                    "My House"
                                )
                            ) { houseId ->
                                Config.currentHouseId = houseId
                                isSuccess.postValue(true)
                            }
                        } else {
                            Config.currentHouseId = houses[0].id
                            isSuccess.postValue(true)
                        }
                    }
                },
                onFailure = { code, message ->
                    onError(mapOf(ResponseCode.generateUser to message))
                    isSuccess.postValue(false)
                }
            )
        }
        return isSuccess
    }

    private fun fetchHouseData() {

    }
}