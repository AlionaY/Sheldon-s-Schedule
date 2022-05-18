package com.pti.sheldons_schedule.ui.screens.create_event_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomSheetContentText(string: String, onClick: () -> Unit) {
    Text(
        text = string,
        fontSize = 15.sp,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()
            .clickable { onClick() },
        textAlign = TextAlign.Start
    )
}

@Composable
fun BottomSheetContent(
    data: List<Int>?,
    nameGetter: (Int) -> String,
    header: String,
    onClick: (index: Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)
    ) {
        item {
            Text(
                text = header,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
        }

        data?.forEach {
            item {
                BottomSheetContentText(nameGetter(it)) { onClick(data.indexOf(it)) }
            }
        }
    }
}