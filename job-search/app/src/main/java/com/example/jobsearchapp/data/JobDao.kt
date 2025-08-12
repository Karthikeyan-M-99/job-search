package com.example.jobsearchapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface JobDao {
    @Insert
    suspend fun insert(job: JobEntity)

    @Query("SELECT * FROM jobs ORDER BY timestamp DESC")
    suspend fun getAll(): List<JobEntity>
}
