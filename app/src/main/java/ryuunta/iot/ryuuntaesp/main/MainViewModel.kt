package ryuunta.iot.ryuuntaesp.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.authentication.AuthenticationHelper
import ryuunta.iot.ryuuntaesp.base.BaseViewModel
import ryuunta.iot.ryuuntaesp.data.model.IconWithTextObj
import ryuunta.iot.ryuuntaesp.data.network.RetrofitService

class MainViewModel(): BaseViewModel() {

    private val _gooogleUserInfo = MutableLiveData<GoogleSignInAccount>()
    val googleUserInfo : LiveData<GoogleSignInAccount> = _gooogleUserInfo


    val listHomeUser = listOf(
        IconWithTextObj(0, R.drawable.ic_no_face, "Ryuunta"),
        IconWithTextObj(1, R.drawable.klee, "Ryuunta 2"),
        IconWithTextObj(2, R.drawable.ic_no_face, "Ryuunta 3")
    )

}