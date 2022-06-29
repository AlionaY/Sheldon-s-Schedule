package com.pti.sheldons_schedule.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pti.sheldons_schedule.data.Event
import com.pti.sheldons_schedule.util.Constants.EVENT
import com.pti.sheldons_schedule.util.Constants.REMINDER_ID
import javax.inject.Inject

class AlarmBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationController: NotificationController

    override fun onReceive(context: Context?, intent: Intent?) {
        notificationController = NotificationController(context)
        val reminderId = intent?.getLongExtra(REMINDER_ID, 0)
        val event = intent?.getParcelableExtra<Event?>(EVENT)
        val reminderServiceIntent = Intent(context, NotificationController::class.java).apply {
            putExtra(REMINDER_ID, reminderId)
            putExtra(EVENT, event)
        }
        context?.startService(reminderServiceIntent)
        notificationController.createNotification(event)
    }
}