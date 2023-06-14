package com.example.top5apps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
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
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            for (i in 0 until 5) {
                if (!Objects.isNull(usageStatistics[i])) {
                    val mostUsedApp = usageStatistics[i];
                    val packageName = mostUsedApp.packageName
                    val applicationLabel = mostUsedApp.applicationlabel
                    val timeForeground = mostUsedApp.timeForeground
                    val launchCount = mostUsedApp.launchCount
                }
            }

//            for (i in 0..5) {
//                val mostUsedApp = usageStatistics.firstOrNull()
//                val packageName = mostUsedApp?.packageName
//                val applicationLabel = mostUsedApp?.applicationlabel
//                val timeForeground = mostUsedApp?.timeForeground
//                val launchCount = mostUsedApp?.launchCount
//
//                if (Objects.isNull(mostUsedApp)) {
//                    print("It's null");
//                } else {
//                    print(packageName);
//                }
//            }

        }
    }
}