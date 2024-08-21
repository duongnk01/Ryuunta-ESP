package ryuunta.iot.ryuuntaesp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ryuunta.iot.ryuuntaesp.data.dao.UserDao
import ryuunta.iot.ryuuntaesp.data.entity.User

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
}