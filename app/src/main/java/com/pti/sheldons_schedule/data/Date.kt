package com.pti.sheldons_schedule.data

data class Date(
    val year: Int,
    val month: Int,
    val day: Int,
    val formattedDate: String = ""
) {
    fun toFormat() =
        "${this.day}/${this.month + 1}/${this.year}"
}
