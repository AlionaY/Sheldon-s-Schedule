package com.pti.sheldons_schedule.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DefaultCheckboxRow(
    checked: Boolean,
    text: String,
    textStyle: TextStyle,
    isClicked: Boolean,
    onValueChanged: (String) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember {FocusRequester()}
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = isClicked) {
        if (isClicked) {
            focusManager.clearFocus()
            focusRequester.requestFocus()
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onCheckedChange(it) },
            modifier = Modifier.wrapContentSize()
        )
        BasicTextField(
            value = text,
            onValueChange = { onValueChanged(it) },
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .wrapContentHeight()
                .fillMaxWidth()
                .focusRequester(focusRequester),
            textStyle = textStyle
        )
    }
}

@Composable
fun IconedText(
    text: String,
    textSize: TextUnit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Filled.Add
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .wrapContentSize()
                .clickable { onClick() },
            tint = MaterialTheme.colors.onBackground
        )
        Text(
            text = text,
            fontSize = textSize,
            modifier = Modifier
                .padding(start = 15.dp)
                .wrapContentSize()
                .clickable { onClick() }
        )
    }
}

@Composable
fun DefaultBottomSheetField(
    text: String?,
    label: String,
    onClick: () -> Unit,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = text.orEmpty(),
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
    value: TextFieldValue,
    label: String,
    onValueChanged: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    errorText: String? = null,
    borderColor: Color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
) {
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChanged(it) },
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
            Text(
                text = errorText.orEmpty(),
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(start = 16.dp),
                fontSize = 13.sp
            )
        }
    }
}