package com.pti.sheldons_schedule.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pti.sheldons_schedule.util.Constants

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val reminderId = intent?.getLongExtra(Constants.REMINDER_ID, 0)
        val isServiceRunning = intent?.getBooleanExtra(Constants.IS_SERVICE_RUNNING, false)
        val reminderServiceIntent = Intent(context, NotificationController::class.java).apply {
            putExtra(Constants.REMINDER_ID, reminderId)
        }
        isServiceRunning?.let {
            if (!it) context?.startService(reminderServiceIntent)
        }
    }
}