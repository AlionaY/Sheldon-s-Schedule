package com.pti.sheldons_schedule.ui.screens.create_event_screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.ui.theme.Red
import com.pti.sheldons_schedule.ui.theme.Steel

@Composable
fun DefaultBottomSheetField(string: String, onClick: () -> Unit) {
    Text(
        text = string,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 15.dp)
            .clickable { onClick() }
            .border(
                width = 0.5.dp,
                color = Steel,
                shape = RoundedCornerShape(10)
            ),
        color = Steel,
        textAlign = TextAlign.Start
    )
}

@Composable
fun DefaultFieldHeader(
    header: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = header.uppercase(),
        modifier = modifier,
        textAlign = TextAlign.Start,
        fontSize = 10.sp,
        color = Steel
    )
}

@Composable
fun HeightSpacer(height: Dp = 20.dp) {
    Spacer(
        modifier = Modifier
            .height(height)
            .fillMaxWidth()
    )
}

@Composable
fun DefaultTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChanged(it) },
        label = { Text(label) },
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun SaveOrCloseCreatingEvent(
    onCloseIconClicked: () -> Unit,
    onSaveIconClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 15.dp)
                    .wrapContentSize()
                    .clickable { onCloseIconClicked() },
                tint = Red
            )

            Text(
                text = stringResource(id = R.string.support),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(start = 25.dp),
                style = MaterialTheme.typography.h6
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_done),
            contentDescription = null,
            modifier = Modifier
                .wrapContentHeight()
                .padding(end = 15.dp)
                .clickable { onSaveIconClicked() },
            tint = Red
        )
    }
}