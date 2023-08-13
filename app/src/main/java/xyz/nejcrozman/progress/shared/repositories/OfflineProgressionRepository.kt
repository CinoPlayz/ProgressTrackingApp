package xyz.nejcrozman.progress.shared.repositories

import kotlinx.coroutines.flow.Flow
import xyz.nejcrozman.progress.shared.daos.ProgressionDao
import xyz.nejcrozman.progress.shared.entities.Progression
import java.time.LocalDateTime

class OfflineProgressionRepository(private val progressionDao: ProgressionDao): ProgressionRepository {
    override suspend fun insert(progression: Progression) = progressionDao.insert(progression)

    override suspend fun update(progression: Progression) = progressionDao.update(progression)

    override suspend fun delete(progression: Progression) = progressionDao.delete(progression)

    override suspend fun deleteById(id: Int) = progressionDao.deleteById(id)

    override fun getProgression(id: Int): Flow<Progression> = progressionDao.getProgression(id)

    override fun getProgressionByTypeId(id: Int): Flow<List<Progression>> = progressionDao.getProgressionsByTypeId(id)

    override fun getAllProgressions(): Flow<List<Progression>> = progressionDao.getAllProgressions()

    override fun getProgressionPreviousValue(type_id: Int, dateTime: LocalDateTime): Int = progressionDao.getProgressionPreviousValue(type_id, dateTime)
}