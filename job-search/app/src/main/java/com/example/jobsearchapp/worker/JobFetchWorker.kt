package com.example.jobsearchapp.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.jobsearchapp.data.JobRepository
import com.example.jobsearchapp.network.OpenAIClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JobFetchWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    private val repo = JobRepository.getInstance(context)

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val prefs = applicationContext.getSharedPreferences("job_prefs", Context.MODE_PRIVATE)
            val apiKey = prefs.getString("openai_key", "") ?: ""
            if (apiKey.isNullOrBlank()) return@withContext Result.failure()

            val roles = prefs.getString("roles", "React.js Developer, Full Stack Developer, Software Developer, Go Developer, Node.js Developer")!!
            val locations = prefs.getString("locations", "Coimbatore, Bangalore")!!
            val prompt = buildPrompt(roles, locations)

            val client = OpenAIClient(apiKey)
            val responseText = client.getChatCompletion(prompt)

            // TODO: parse responseText into Job entities; for now store raw text
            repo.saveRawResult(responseText)

            sendNotification("JobSearchApp", "New job results available. Tap to open the app.")

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    private fun buildPrompt(roles: String, locations: String): String {
        return """At 8:00 AM every day, search for job openings for ${roles} in ${locations}.
Use LinkedIn, Naukri, Indeed, and any relevant Telegram/WhatsApp job groups.
Include both walk-in and online application opportunities.
Only show jobs posted within the last 24 hours.
Present results so that walk-in jobs are listed first, followed by online application jobs.
For each job, include:
- Job title
- Company name
- Location
- Date posted
- Source link
- Apply link (direct link to job application page)
- Job description (key responsibilities and requirements)
- Whether it’s walk-in or online application
Present the results in a neat bullet-point format."""
    }

    private fun sendNotification(title: String, text: String) {
        val nm = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "job_search_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = NotificationChannel(channelId, "Job Search", NotificationManager.IMPORTANCE_DEFAULT)
            nm.createNotificationChannel(ch)
        }
        val n = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()
        nm.notify(1001, n)
    }
}
