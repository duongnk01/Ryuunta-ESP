package ryuunta.iot.ryuuntaesp.core.callback

import ryuunta.iot.ryuuntaesp.data.model.UserInfo

class AuthRequestCallbackWrapper {
    private var onSuccess: (UserInfo) -> Unit = {}
    private var onFailure: (code: Int, message: String) -> Unit = { _, _ -> }
    private var userInfo: UserInfo? = null
    private var failureCode: Int? = null
    private var failureMess: String? = null

    internal val callback = object : AuthRequestCallback {
        override fun onSuccess(userInfo: UserInfo) {
            this@AuthRequestCallbackWrapper.onSuccess
            if (this@AuthRequestCallbackWrapper.userInfo == null) {
                this@AuthRequestCallbackWrapper.userInfo = UserInfo("Ryuunta ESP", "")
            }
        }

        override fun onFailure(code: Int, message: String) {
            this@AuthRequestCallbackWrapper.onFailure(code, message)
            if (failureCode == null) {
                failureCode = code
                failureMess = message
            }
        }

    }

    fun onSuccess(func: (UserInfo) -> Unit) : AuthRequestCallbackWrapper {
        onSuccess = func
        if (userInfo != null) func(userInfo!!)
        return this
    }

    fun onFailure(func: (code: Int, message: String) -> Unit) : AuthRequestCallbackWrapper {
        onFailure = func
        if (failureCode != null)func(failureCode!!, failureMess!!)
        return this
    }
}

interface AuthRequestCallback {
    fun onSuccess(userInfo: UserInfo)
    fun onFailure(code: Int, message: String)
}