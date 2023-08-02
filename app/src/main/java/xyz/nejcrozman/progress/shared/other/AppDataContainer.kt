package xyz.nejcrozman.progress.shared.other

import android.content.Context
import xyz.nejcrozman.progress.shared.databases.ProgressDatabase
import xyz.nejcrozman.progress.shared.repositories.OfflineTypeRepository
import xyz.nejcrozman.progress.shared.repositories.TypeRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val typeRepository: TypeRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineTypeRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    override val typeRepository: TypeRepository by lazy {
        OfflineTypeRepository(ProgressDatabase.getDatabase(context).typeDeo())
    }
}