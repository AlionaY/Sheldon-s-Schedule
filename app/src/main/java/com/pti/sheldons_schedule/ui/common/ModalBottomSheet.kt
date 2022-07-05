package com.pti.sheldons_schedule.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> ModalBottomSheet(
    data: List<T>,
    header: String,
    nameGetter: @Composable (T) -> String,
    onClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (state: ModalBottomSheetState) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetContent = {
            Box(modifier = modifier.navigationBarsPadding()) {
                BottomSheetContent(
                    data = data,
                    header = header,
                    nameGetter = { nameGetter(it) }
                ) {
                    onClick(it)
                    scope.launch { sheetState.hide() }
                }
            }
        },
        sheetState = sheetState,
        sheetBackgroundColor = MaterialTheme.colors.background,
        sheetShape = RoundedCornerShape(3)
    ) {
        content(sheetState)
    }
}