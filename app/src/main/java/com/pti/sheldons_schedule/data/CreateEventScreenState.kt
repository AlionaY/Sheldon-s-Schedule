package com.pti.sheldons_schedule.data

import com.pti.sheldons_schedule.util.Constants.DATE_FORMAT
import com.pti.sheldons_schedule.util.formatDate
import java.util.*

data class CreateEventScreenState(
    val startDate: Calendar,
    val endDate: Calendar
) {
    val formattedStartDate: String = startDate.formatDate(DATE_FORMAT)
    val formattedEndDate: String = endDate.formatDate(DATE_FORMAT)

    override fun equals(other: Any?) : Boolean {
        return false
    }

    override fun hashCode(): Int {
        var result = startDate.hashCode()
        result = 31 * result + endDate.hashCode()
        result = 31 * result + formattedStartDate.hashCode()
        result = 31 * result + formattedEndDate.hashCode()
        return result
    }
}