package com.demosportsapl.app

import android.content.Context
import com.demosportsapl.app.data.local.datastore.SessionDataStore
import com.demosportsapl.app.data.local.db.AppDatabase
import com.demosportsapl.app.data.repository.*

object ServiceLocator {
    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    val database: AppDatabase by lazy { AppDatabase.getInstance(appContext) }
    val sessionDataStore: SessionDataStore by lazy { SessionDataStore(appContext) }

    val tableRepository: TableRepository by lazy { TableRepository(database.tableDao()) }
    val sportRepository: SportsRepository by lazy { SportsRepository(database.sportDao()) }
    val fixtureRepository: FixtureRepository by lazy { FixtureRepository(database.fixtureDao()) }
    val resultRepository: ResultRepository by lazy { ResultRepository(database.resultDao()) }
    val rosterRepository: RosterRepository by lazy { RosterRepository(database.rosterDao()) }
    val auditLogRepository: AuditLogRepository by lazy { AuditLogRepository(database.auditLogDao()) }
    val alertRepository: AlertRepository by lazy { AlertRepository(database.alertDao()) }
    val syncQueueRepository: SyncQueueRepository by lazy { SyncQueueRepository(database.syncQueueDao()) }
}
