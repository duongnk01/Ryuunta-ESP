package ryuunta.iot.ryuuntaesp.data.network

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ryuunta.iot.ryuuntaesp.data.entity.User
import ryuunta.iot.ryuuntaesp.data.model.RootResult

interface RetrofitService {
    @POST("login")
    suspend fun login(@Body user: User) : Response<GoogleSignInAccount>
}