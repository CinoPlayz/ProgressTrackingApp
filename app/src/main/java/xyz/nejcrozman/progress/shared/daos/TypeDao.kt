package xyz.nejcrozman.progress.shared.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import xyz.nejcrozman.progress.shared.entities.Type

@Dao
interface TypeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(type: Type)

    @Update
    suspend fun update(type: Type)

    @Delete
    suspend fun delete(type: Type)

    @Query("DELETE FROM types WHERE type_id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * from types WHERE type_id = :id")
    fun getType(id: Int): Flow<Type>

    @Query("SELECT * from types ORDER BY name ASC")
    fun getAllTypes(): Flow<List<Type>>





}