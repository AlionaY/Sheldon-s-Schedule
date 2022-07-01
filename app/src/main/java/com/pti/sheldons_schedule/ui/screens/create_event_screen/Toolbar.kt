package com.pti.sheldons_schedule.ui.screens.create_event_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pti.sheldons_schedule.R

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
                tint = MaterialTheme.colors.error
            )

            Text(
                text = stringResource(id = R.string.support),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(start = 25.dp),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onBackground
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_done),
            contentDescription = null,
            modifier = Modifier
                .wrapContentHeight()
                .padding(end = 15.dp)
                .clickable { onSaveIconClicked() },
            tint = MaterialTheme.colors.secondary
        )
    }
}