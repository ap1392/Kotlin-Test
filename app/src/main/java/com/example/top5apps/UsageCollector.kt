package com.example.top5apps

import android.app.Activity
import android.app.usage.UsageEvents.Event
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.getSystemService
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.Iterator


class UsageCollector {
    val TAG = "USG-BB"
    fun getInstalledApps(context: Context): List<PackageInfo>{
        val packageManager = context.packageManager
        return packageManager.getInstalledPackages(0)
    }

    fun getUsageStatistics(context: Context, startTime: Long, endTime: Long): ArrayList<ApplicationStatistics> {
        var statisticsMap = HashMap<String, ApplicationStatistics>()
        var sameEvents = HashMap<String, ArrayList<Event>>()
        val applicationStatistics = ArrayList<ApplicationStatistics>()

        var mUsageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        Log.d(TAG, "Querying for events between $startTime and $endTime")
        var usageEvents = mUsageStatsManager.queryEvents(startTime, endTime)
        Log.d(TAG, usageEvents.toString())
        while (usageEvents.hasNextEvent()){
            Log.d(TAG, "Reading next event")
            var currentEvent = Event()
            usageEvents.getNextEvent(currentEvent)
            if (currentEvent.eventType == Event.ACTIVITY_RESUMED || currentEvent.eventType == Event.ACTIVITY_PAUSED) {
                var key = currentEvent.packageName
                // If the key does not exist, we create a new object for the stats and for the list of events
                if (statisticsMap[key] == null) {
                    statisticsMap[key] = ApplicationStatistics(key)
                    sameEvents[key] = ArrayList<Event>()
                }
                // add the relevant events
                sameEvents[key]?.add(currentEvent)
            }
        }

        // If we found no events on the time frame, avoid looping through installed packages
        if (sameEvents.size > 0){
            Log.d(TAG, "Looping through installed packages")
            getInstalledApps(context).forEach {
                var packageInfo = it
                var packageName = packageInfo.packageName
                Log.d(TAG, "Checking stats for $packageName")
                // If null it means there are no events for this package
                if (statisticsMap[packageName] != null){
                    statisticsMap[packageName]?.applicationlabel =
                        context.packageManager.
                        getApplicationLabel(packageInfo.applicationInfo) as String
                    var totalEvents = sameEvents[packageName]!!.size
                    var i = 0
                    Log.d(TAG, "We found $totalEvents events for the package")
                    while (i < totalEvents){
                        // We get the current event and check if it was launched
                        var event0 = sameEvents[packageName]?.get(i)
                        if (event0 != null && event0.eventType == Event.ACTIVITY_RESUMED){
                            statisticsMap[event0.packageName]!!.launchCount += 1
                        }
                        // If there is one more event after, we check it
                        if (i + 1 < totalEvents ) {
                            var event1 = sameEvents[packageName]?.get(i + 1)
                            // We check if the next event is an activity paused, after an activity resumed
                            if (event1 != null && event0 != null && event0.eventType == Event.ACTIVITY_RESUMED && event1.eventType == Event.ACTIVITY_PAUSED) {
                                var diff = event1.timeStamp - event0.timeStamp
                                statisticsMap[event0.packageName]!!.timeForeground += diff
                            }
                        }
                        i += 1
                    }

                    // If the first event was activity paused, we add whatever time has passed as foreground
                    // TODO: Are the packageNames different? Check and make this clearer if possible
                    if (sameEvents.get(packageName)?.get(0)?.eventType  == Event.ACTIVITY_PAUSED){
                        val diff = sameEvents.get(packageName)!!.get(0).timeStamp - startTime
                        statisticsMap.get(sameEvents.get(packageName)!!.get(0).packageName)!!.timeForeground += diff
                    }

                    // If the last event is activity started, we also add this time as foreground
                    // TODO: Are the packageNames different? Check and make this clearer if possible
                    if (sameEvents[packageName]?.get(totalEvents - 1)!!.eventType == Event.ACTIVITY_RESUMED) {
                        val diff = endTime - sameEvents[packageName]!![totalEvents - 1].timeStamp
                        statisticsMap.get(sameEvents.get(packageName)!!.get(totalEvents-1).packageName)!!.timeForeground += diff
                    }

                    statisticsMap[packageInfo.packageName]?.let {
                            it1 -> applicationStatistics.add(it1)
                        Log.d(TAG, "Adding new app statistic")
                    }

                }
            }

        }



        return applicationStatistics


    }

}
