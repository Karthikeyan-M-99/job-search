package com.example.jobsearchapp.ui

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.jobsearchapp.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val etApiKey = findViewById<EditText>(R.id.etApiKey)
        val etRoles = findViewById<EditText>(R.id.etRoles)
        val etLocations = findViewById<EditText>(R.id.etLocations)
        val btnSave = findViewById<Button>(R.id.btnSave)

        val prefs = getSharedPreferences("job_prefs", Context.MODE_PRIVATE)
        etApiKey.setText(prefs.getString("openai_key", ""))
        etRoles.setText(prefs.getString("roles", "React.js Developer, Full Stack Developer, Software Developer, Go Developer, Node.js Developer"))
        etLocations.setText(prefs.getString("locations", "Coimbatore, Bangalore"))

        btnSave.setOnClickListener {
            prefs.edit().putString("openai_key", etApiKey.text.toString())
                .putString("roles", etRoles.text.toString())
                .putString("locations", etLocations.text.toString())
                .apply()
            finish()
        }
    }
}
