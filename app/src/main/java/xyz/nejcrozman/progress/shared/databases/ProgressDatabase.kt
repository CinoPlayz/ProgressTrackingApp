package xyz.nejcrozman.progress.shared.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import xyz.nejcrozman.progress.shared.daos.TypeDao
import xyz.nejcrozman.progress.shared.entities.Type

@Database(entities = [Type::class], version = 1, exportSchema = false)
abstract class ProgressDatabase: RoomDatabase() {
    abstract fun typeDeo(): TypeDao
    companion object {
        @Volatile
        private var Instance: ProgressDatabase? = null

        fun getDatabase(context: Context): ProgressDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ProgressDatabase::class.java, "progress_database").fallbackToDestructiveMigration().build()
                    .also { Instance = it }

            }
        }

    }

}