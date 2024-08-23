package ryuunta.iot.ryuuntaesp.data.network

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ryuunta.iot.ryuuntaesp.data.entity.User
import ryuunta.iot.ryuuntaesp.data.model.WeatherDataCompilation

interface RetrofitService {
    @POST("login")
    suspend fun login(@Body user: User) : Response<GoogleSignInAccount>

    @GET(UrlManage.getCurrentWeather)
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String,
        @Query("lang") lang: String = "vi"
    ) : Response<WeatherDataCompilation.WeatherData>

    @GET(UrlManage.getCurrAirPollution)
    suspend fun getCurrAirPollution(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String,
    ) : Response<WeatherDataCompilation.AirPollution>

}