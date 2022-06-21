package com.pti.sheldons_schedule.ui.screens.create_event_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pti.sheldons_schedule.R

@Composable
fun DefaultBottomSheetField(
    string: String,
    label: String,
    onClick: () -> Unit,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = string,
        label = { Text(label) },
        onValueChange = { onValueChanged(it) },
        modifier = modifier
            .onFocusChanged { if (it.hasFocus) onClick() }
            .clickable { onClick() },
        readOnly = true
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
    label: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorText: String? = null,
    borderColor: Color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
) {
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChanged(it.trim()) },
            trailingIcon = {
                if (!errorText.isNullOrEmpty()) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "error",
                        tint = MaterialTheme.colors.error
                    )
                }
            },
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = MaterialTheme.colors.onBackground,
                cursorColor = MaterialTheme.colors.onBackground,
                backgroundColor = Color.Transparent,
                unfocusedBorderColor = borderColor
            )
        )

        if (!errorText.isNullOrEmpty()) {
            ErrorTextMessage(errorText, Modifier.padding(start = 16.dp))
        }
    }
}

@Composable
fun ErrorTextMessage(
    errorText: String?,
    modifier: Modifier = Modifier,
    textSize: TextUnit = 13.sp
) {
    Text(
        text = errorText.orEmpty(),
        color = MaterialTheme.colors.error,
        modifier = modifier,
        fontSize = textSize
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