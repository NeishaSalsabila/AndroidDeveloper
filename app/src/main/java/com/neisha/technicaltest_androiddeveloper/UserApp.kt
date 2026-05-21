package com.neisha.technicaltest_androiddeveloper

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class UserApp : Application() {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        val config = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
        try {
            WorkManager.initialize(this, config)
        } catch (_: IllegalStateException) {
        }
    }
}
