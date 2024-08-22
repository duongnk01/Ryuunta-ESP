package ryuunta.iot.ryuuntaesp.authentication

sealed class AuthenticationState {
    data class Success(val isSuccess: Boolean) : AuthenticationState()
    data class SignUpSuccess(val email: String, val password: String) : AuthenticationState()
    data class Failure(val code: Int, val message: String?) : AuthenticationState()
}
