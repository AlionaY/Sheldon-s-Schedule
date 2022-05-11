package com.pti.sheldons_schedule.ui.screens.create_event_screen

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.ui.theme.Red

@Composable
fun DefaultSpacer(modifier: Modifier = Modifier, height: Int = 20) {
    Spacer(
        modifier = modifier
            .height(height.dp)
            .fillMaxWidth()
    )
}

@Composable
fun DefaultTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    @StringRes labelRes: Int,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChanged(it) },
        label = { Text(stringResource(id = labelRes)) },
        modifier = modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
    )
}

@Composable
fun SaveOrCloseCreatingEvent(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(58.dp)
            .fillMaxWidth(),
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
                modifier = modifier
                    .padding(start = 15.dp)
                    .wrapContentSize()
                    .clickable {  },
                tint = Red
            )

            Text(
                text = stringResource(id = R.string.support),
                modifier = modifier
                    .wrapContentSize()
                    .padding(start = 25.dp),
                style = MaterialTheme.typography.h6
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_done),
            contentDescription = null,
            modifier = modifier
                .wrapContentHeight()
                .padding(end = 15.dp)
                .clickable {  },
            tint = Red
        )
    }
}