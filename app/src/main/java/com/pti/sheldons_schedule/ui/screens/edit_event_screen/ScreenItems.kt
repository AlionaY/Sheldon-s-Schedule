package com.pti.sheldons_schedule.ui.screens.edit_event_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.data.ScreenState
import com.pti.sheldons_schedule.ui.common.DefaultCheckboxRow
import com.pti.sheldons_schedule.ui.common.HeightSpacer
import com.pti.sheldons_schedule.ui.common.IconedText
import com.pti.sheldons_schedule.util.Constants

@Composable
fun IconTextButton(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colors.onBackground,
    textSize: TextUnit = 12.sp,
    textColor: Color = MaterialTheme.colors.onBackground
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .padding(15.dp)
                .align(Alignment.TopCenter),
            tint = iconTint
        )
        Text(
            text = text,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 5.dp),
            fontSize = textSize,
            color = textColor
        )
    }
}