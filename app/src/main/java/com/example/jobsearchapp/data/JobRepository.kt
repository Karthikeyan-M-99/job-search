package com.example.jobsearchapp.data

import android.content.Context

class JobRepository private constructor(private val db: AppDatabase) {
    suspend fun saveRawResult(raw: String) {
        db.jobDao().insert(JobEntity(rawText = raw))
    }

    suspend fun getAll() = db.jobDao().getAll()

    companion object {
        @Volatile private var INSTANCE: JobRepository? = null
        fun getInstance(ctx: Context): JobRepository {
            return INSTANCE ?: synchronized(this) {
                val inst = JobRepository(AppDatabase.getInstance(ctx))
                INSTANCE = inst
                inst
            }
        }
    }
}
