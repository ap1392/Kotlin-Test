package com.example.top5apps

import kotlin.properties.Delegates

class ApplicationStatistics(val packageName: String) {
    lateinit var applicationlabel: String
    var timeForeground: Long = 0
    var launchCount = 0
    var time = System.currentTimeMillis()

    override fun toString(): String {
        val second = timeForeground / 1000 % 60
        val minute = timeForeground / (1000 * 60) % 60
        return packageName + " " + minute.toString() + "m " + second + "s "
    }

}
