package com.pti.sheldons_schedule.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pti.sheldons_schedule.data.Event
import com.pti.sheldons_schedule.util.Constants.EVENT
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationController: NotificationController

    override fun onReceive(context: Context?, intent: Intent?) {
        val event = intent?.getParcelableExtra<Event?>(EVENT)
        notificationController.createNotification(event)
    }
}