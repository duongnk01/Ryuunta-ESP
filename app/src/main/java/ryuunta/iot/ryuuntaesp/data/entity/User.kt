package ryuunta.iot.ryuuntaesp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey val googleId: Int,
    val email: String,
    val name: String,
    val profileImageUrl: String
)
