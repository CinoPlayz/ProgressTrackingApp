package xyz.nejcrozman.progress.shared.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "progression", foreignKeys = [ForeignKey(entity = Type::class,
        parentColumns = arrayOf("type_id"),
    childColumns = arrayOf("FK_type_id"),
    onDelete = CASCADE)])
@Parcelize
data class Progression (
    @PrimaryKey(autoGenerate = true)
    val progress_id: Int = 0,
    val FK_type_id: Int = 0,
    val value: Int = 1,
    val dateOfProgress: LocalDateTime,
    val dateOfLastEdit: LocalDateTime = LocalDateTime.now()
): Parcelable {
    /**
     * Converts [dateOfProgress] to string formatted like (dd.MM.yyyy)
     */
    val getDOPFormatted : String
        get() =dateOfProgress.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

    /**
     * Converts [dateOfLastEdit] to string formatted like (dd.MM.yyyy)
     */
    val getDOLEFormatted : String
        get() =dateOfLastEdit.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

}