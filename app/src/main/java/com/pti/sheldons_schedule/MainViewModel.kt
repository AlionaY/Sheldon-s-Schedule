package com.pti.sheldons_schedule

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    companion object {
        const val DATE_FORMAT = "dd/MM/yyyy"
    }

    val date = MutableStateFlow(getCurrentDate())


    private fun getCurrentDate(): String? {
        val calendar = Calendar.getInstance()
        return formatDate(calendar.timeInMillis)
    }

    fun onDatePicked(millis: Long) {
        date.value = formatDate(millis)
    }

    private fun formatDate(milliseconds: Long?): String? {
        milliseconds?.let {
            val formatter = SimpleDateFormat(DATE_FORMAT, Locale.US)
            val calendar: Calendar = Calendar.getInstance()
            calendar.timeInMillis = it
            return formatter.format(calendar.time)
        }
        return null
    }
}
