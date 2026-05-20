package com.neisha.technicaltest_androiddeveloper.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.neisha.technicaltest_androiddeveloper.domain.usecase.RefreshCitiesUseCase
import com.neisha.technicaltest_androiddeveloper.domain.usecase.RefreshUsersUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val refreshUsersUseCase: RefreshUsersUseCase,
    private val refreshCitiesUseCase: RefreshCitiesUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            refreshUsersUseCase()
            refreshCitiesUseCase()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "SyncWorker"
    }
}
