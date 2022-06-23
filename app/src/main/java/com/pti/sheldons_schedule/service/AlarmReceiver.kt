package com.pti.sheldons_schedule.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pti.sheldons_schedule.data.Event
import com.pti.sheldons_schedule.util.Constants

class AlarmReceiver : BroadcastReceiver() {

    private lateinit var notificationController: NotificationController

    override fun onReceive(context: Context?, intent: Intent?) {
        notificationController = NotificationController(context)
        val reminderId = intent?.getLongExtra(Constants.REMINDER_ID, 0)
        val event = intent?.getParcelableExtra<Event?>(Constants.EVENT)
        val reminderServiceIntent = Intent(context, NotificationController::class.java).apply {
            putExtra(Constants.REMINDER_ID, reminderId)
            putExtra(Constants.EVENT, event)
        }
        context?.startService(reminderServiceIntent)
        event?.let { notificationController.createNotification(it) }
    }
}