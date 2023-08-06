package xyz.nejcrozman.progress.shared.repositories

import kotlinx.coroutines.flow.Flow
import xyz.nejcrozman.progress.shared.daos.TypeDao
import xyz.nejcrozman.progress.shared.entities.Type

class OfflineTypeRepository(private val typeDao: TypeDao): TypeRepository {
    override fun getType(id: Int): Flow<Type> = typeDao.getType(id)

    override fun getAllTypes(): Flow<List<Type>> = typeDao.getAllTypes()

    override suspend fun insert(type: Type) = typeDao.insert(type)

    override suspend fun update(type: Type) = typeDao.update(type)

    override suspend fun delete(type: Type) = typeDao.delete(type)

    override suspend fun deleteById(id: Int)  = typeDao.deleteById(id)
}