package com.pti.sheldons_schedule.ui.screens.main_screen

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.db.EventRepository
import com.pti.sheldons_schedule.util.Constants
import com.pti.sheldons_schedule.util.Constants.CHANNEL_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.*
import javax.inject.Inject


@OptIn(ObsoleteCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: EventRepository,
    @ApplicationContext val context: Context
) : ViewModel() {

    companion object {
        private const val MINUTES_IN_HOUR_FLOAT = 60f
        private const val MINUTE_LONG = 60000L
    }

    val weeks = Pager(PagingConfig(1)) {
        WeekdaysPagingSource()
    }.flow.cachedIn(viewModelScope)

    val ticker = ticker(
        delayMillis = MINUTE_LONG,
        initialDelayMillis = 0,
        context = viewModelScope.coroutineContext
    ).receiveAsFlow().map {
        val calendar = Calendar.getInstance()
        val currentMinutes = calendar.get(Calendar.MINUTE)
        val currentMinutesInPercent = (currentMinutes / MINUTES_IN_HOUR_FLOAT)
        currentMinutesInPercent
    }


    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val name = context.getString(R.string.notification_channel_name)
        val descriptionText = context.getString(R.string.notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager? = context.getSystemService<NotificationManager>()
        notificationManager?.createNotificationChannel(channel)
    }
}