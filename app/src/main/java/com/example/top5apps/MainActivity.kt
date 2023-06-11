package com.example.top5apps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000 // 24 hours ago
        val endTime = System.currentTimeMillis() // current time
        val usageCollector = UsageCollector();
        val usageStatistics = usageCollector.getUsageStatistics(this, startTime, endTime)
        usageStatistics.sortByDescending { it.timeForeground }
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            println("Hello World")
        }

//        val test = usageStatistics.get(0);
//
//        println(usageStatistics);
    }
}