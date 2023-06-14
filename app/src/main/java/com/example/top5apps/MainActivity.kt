package com.example.top5apps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.util.Objects


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000 // 24 hours ago
        val endTime = System.currentTimeMillis() // current time
        val usageCollector = UsageCollector();
        val usageStatistics = usageCollector.getUsageStatistics(this, startTime, endTime)
        usageStatistics.sortByDescending { it.timeForeground }
        val top5AppsTextView = findViewById<TextView>(R.id.top5Apps)
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            for (i in 0 until 5) {
                if (!Objects.isNull(usageStatistics[i])) {
                    val mostUsedApp = usageStatistics[i];
                    val applicationLabel = mostUsedApp.applicationlabel
                    val timeForeground = mostUsedApp.timeForeground
                    val launchCount = mostUsedApp.launchCount
                    val existingText = top5AppsTextView.text.toString()
                    val curApp = i + 1;
                    val timeInMinutes = timeForeground / 1000 / 60
                    val hours = timeInMinutes / 60
                    val minutes = timeInMinutes % 60
                    val formattedText = "$existingText$curApp) $applicationLabel: used for $hours hours and $minutes minutes. It was opened $launchCount times.\n \n"
                    top5AppsTextView.text = formattedText
                }
            }

        }
    }
}