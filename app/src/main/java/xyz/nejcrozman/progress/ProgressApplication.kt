package xyz.nejcrozman.progress

import android.app.Application
import xyz.nejcrozman.progress.shared.other.AppContainer
import xyz.nejcrozman.progress.shared.other.AppDataContainer

class ProgressApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}