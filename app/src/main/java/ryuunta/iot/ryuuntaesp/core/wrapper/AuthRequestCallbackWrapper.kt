package ryuunta.iot.ryuuntaesp.core.wrapper

class AuthRequestCallbackWrapper {
    private var onSuccess: (Boolean) -> Unit = {}
    private var onFailure: (code: Int, message: String) -> Unit = { _, _ -> }
    private var success: Boolean? = null
    private var failureCode: Int? = null
    private var failureMess: String? = null

    internal val callback = object : AuthRequestCallback {
        override fun onSuccess() {
            this@AuthRequestCallbackWrapper.onSuccess
            if (this@AuthRequestCallbackWrapper.success == null) {
                this@AuthRequestCallbackWrapper.success = true
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

    fun onSuccess(func: (Boolean) -> Unit) : AuthRequestCallbackWrapper {
        onSuccess = func
        if (success != null) func(success!!)
        return this
    }

    fun onFailure(func: (code: Int, message: String) -> Unit) : AuthRequestCallbackWrapper {
        onFailure = func
        if (failureCode != null)func(failureCode!!, failureMess!!)
        return this
    }
}

interface AuthRequestCallback {
    fun onSuccess()
    fun onFailure(code: Int, message: String)
}