package xyz.nejcrozman.progress.shared.repositories

import kotlinx.coroutines.flow.Flow
import xyz.nejcrozman.progress.shared.entities.Progression
import java.time.LocalDateTime

interface ProgressionRepository {
    suspend fun insert(progression: Progression)

    suspend fun update(progression: Progression)

    suspend fun delete(progression: Progression)

    suspend fun deleteById(id: Int)

    fun getProgression(id: Int): Flow<Progression>

    fun getProgressionByTypeId(id: Int): Flow<List<Progression>>

    fun getProgressionByTypeIdDESC(id: Int): Flow<List<Progression>>

    fun getAllProgressions(): Flow<List<Progression>>

    fun getProgressionPreviousValue(type_id: Int, dateTime: LocalDateTime): Int
}