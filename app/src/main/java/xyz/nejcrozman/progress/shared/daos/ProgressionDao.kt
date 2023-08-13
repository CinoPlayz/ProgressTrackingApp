package xyz.nejcrozman.progress.shared.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import xyz.nejcrozman.progress.shared.entities.Progression
import java.time.LocalDateTime

@Dao
interface ProgressionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(progression: Progression)

    @Update
    suspend fun update(progression: Progression)

    @Delete
    suspend fun delete(progression: Progression)

    @Query("DELETE FROM progression WHERE progress_id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * from progression WHERE progress_id = :id")
    fun getProgression(id: Int): Flow<Progression>

    @Query("SELECT * from progression WHERE FK_type_id = :id ORDER BY dateOfProgress ASC")
    fun getProgressionsByTypeId(id: Int): Flow<List<Progression>>

    @Query("SELECT * from progression ORDER BY progress_id ASC")
    fun getAllProgressions(): Flow<List<Progression>>

    @Query("SELECT value from progression WHERE FK_type_id = :type_id AND dateOfProgress < :dateTime ORDER BY dateOfProgress DESC LIMIT 1")
    fun getProgressionPreviousValue(type_id: Int, dateTime: LocalDateTime): Int
}