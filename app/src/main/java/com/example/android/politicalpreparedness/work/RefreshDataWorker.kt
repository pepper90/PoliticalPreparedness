package com.example.android.politicalpreparedness.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getDatabase
import com.example.android.politicalpreparedness.database.ElectionRepository
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = ElectionRepository(database)
        return try {
            repository.refreshElections()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}