package com.demosportsapl.app

import android.app.Application
import com.demosportsapl.app.data.SeedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class DemosportsApp : Application() {
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        ServiceLocator.init(this)
        appScope.launch {
            SeedData.seedIfNeeded(ServiceLocator.database)
        }
    }
}
