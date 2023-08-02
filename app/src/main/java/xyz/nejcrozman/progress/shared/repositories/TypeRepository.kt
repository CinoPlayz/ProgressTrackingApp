package xyz.nejcrozman.progress.shared.repositories

import kotlinx.coroutines.flow.Flow
import xyz.nejcrozman.progress.shared.entities.Type

interface TypeRepository {
    fun getType(id: Int): Flow<Type>

    fun getAllTypes(): Flow<List<Type>>

    suspend fun insert(type: Type)

    suspend fun update(type: Type)

    suspend fun delete(type: Type)

}