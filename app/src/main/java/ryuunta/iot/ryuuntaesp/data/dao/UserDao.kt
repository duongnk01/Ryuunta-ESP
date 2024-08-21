package ryuunta.iot.ryuuntaesp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ryuunta.iot.ryuuntaesp.data.entity.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * from user where googleId = :googleId")
    suspend fun getUserByGoogleId(googleId: String) : User?
}