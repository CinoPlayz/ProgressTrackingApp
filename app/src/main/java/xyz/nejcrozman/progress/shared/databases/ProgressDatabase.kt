package xyz.nejcrozman.progress.shared.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import xyz.nejcrozman.progress.shared.Converters
import xyz.nejcrozman.progress.shared.daos.ProgressionDao
import xyz.nejcrozman.progress.shared.daos.TypeDao
import xyz.nejcrozman.progress.shared.entities.Progression
import xyz.nejcrozman.progress.shared.entities.Type

@Database(entities = [Type::class, Progression::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ProgressDatabase: RoomDatabase() {
    abstract fun typeDao(): TypeDao
    abstract fun progressionDao(): ProgressionDao
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