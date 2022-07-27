package com.pti.sheldons_schedule.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    h6 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        fontFamily = FontFamily.Default
    ),
    h5 = TextStyle(
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal
    ),
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)