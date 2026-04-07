package com.demosportsapl.app.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.demosportsapl.app.data.local.db.dao.*
import com.demosportsapl.app.data.local.db.entities.*

@Database(
    entities = [
        TableEntity::class,
        SportEntity::class,
        FixtureEntity::class,
        ResultEntity::class,
        RosterEntry::class,
        AuditLog::class,
        SyncQueueItem::class,
        AlertEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tableDao(): TableDao
    abstract fun sportDao(): SportDao
    abstract fun fixtureDao(): FixtureDao
    abstract fun resultDao(): ResultDao
    abstract fun rosterDao(): RosterDao
    abstract fun auditLogDao(): AuditLogDao
    abstract fun syncQueueDao(): SyncQueueDao
    abstract fun alertDao(): AlertDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "demosports.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
