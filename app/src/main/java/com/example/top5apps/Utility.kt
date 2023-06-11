package com.example.top5apps

import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

class Utility {
    companion object {
        /**
         * This method checks whether the user has granted Usage access or not
         */
        fun isUsageEnabled(mContext: Context): Boolean {
            val packageManager: PackageManager = mContext.packageManager
            val applicationInfo = packageManager.getApplicationInfo(mContext.packageName, 0)
            val appOpsManager = mContext.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            var mode = 0
            // The new method requires API 29, so we check and use the deprecated one in older devices
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                mode = appOpsManager.unsafeCheckOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    applicationInfo.uid, applicationInfo.packageName
                )
            }
            else{
                mode = appOpsManager.checkOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    applicationInfo.uid, applicationInfo.packageName
                )
            }
            return mode == AppOpsManager.MODE_ALLOWED
        }
    }

}
