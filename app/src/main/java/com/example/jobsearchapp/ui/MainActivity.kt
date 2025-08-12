package com.example.jobsearchapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.jobsearchapp.R
import com.example.jobsearchapp.data.JobRepository
import com.example.jobsearchapp.worker.JobFetchWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var repo: JobRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        repo = JobRepository.getInstance(applicationContext)

        val btnSettings: Button = findViewById(R.id.btnSettings)
        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        val btnFetch: Button = findViewById(R.id.btnFetch)
        val tvStatus: TextView = findViewById(R.id.tvStatus)
        btnFetch.setOnClickListener {
            tvStatus.text = "Queued manual fetch..."
            // enqueue a one-time work - simple approach
            val work = PeriodicWorkRequestBuilder<JobFetchWorker>(1, TimeUnit.DAYS).build()
            WorkManager.getInstance(this).enqueue(work)
        }
    }
}
