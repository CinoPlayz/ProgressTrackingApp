package xyz.nejcrozman.progress.shared.entities
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "types")
data class Type(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)
