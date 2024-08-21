package ryuunta.iot.ryuuntaesp.data.repository

import android.content.Context
import androidx.room.Room
import ryuunta.iot.ryuuntaesp.data.UserDatabase
import ryuunta.iot.ryuuntaesp.data.entity.User

class UserRepository(context: Context) {
    private val database: UserDatabase = Room.databaseBuilder(
        context.applicationContext,
        UserDatabase::class.java,
        "user_database"
    ).build()

    suspend fun insertUser(user: User) = database.userDao().insert(user)

    suspend fun getUserByGoogleId(username: String): User? =
        database.userDao().getUserByGoogleId(username)
}