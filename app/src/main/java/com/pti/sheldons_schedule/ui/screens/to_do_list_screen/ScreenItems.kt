package com.pti.sheldons_schedule.ui.screens.to_do_list_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.ui.theme.Black


@Composable
fun EventTitle(title: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 15.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun TopToolbar(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBackIos,
            contentDescription = "back",
            modifier = Modifier
                .wrapContentSize()
                .clickable { onClick() },
            tint = Black
        )
    }
}

@Composable
fun IconedText(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Add, contentDescription = null,
            modifier = Modifier.wrapContentSize(),
            tint = MaterialTheme.colors.onBackground
        )
        Text(
            text = stringResource(id = R.string.item_of_to_do_list),
            fontSize = 12.sp,
            modifier = Modifier
                .padding(start = 15.dp)
                .wrapContentSize()
        )
    }
}