package com.pti.sheldons_schedule.ui.screens.edit_event_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.ui.screens.edit_event_screen.IconTextButton

@Composable
fun BottomToolbar(
    onSaveEventClicked: () -> Unit,
    onDeleteEventClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (deleteButton, divider, saveButton) = createRefs()

            IconTextButton(
                icon = Icons.Filled.Delete,
                text = stringResource(id = R.string.delete),
                modifier = Modifier
                    .wrapContentSize()
                    .clickable { onDeleteEventClicked() }
                    .constrainAs(deleteButton) {
                        start.linkTo(parent.start)
                        end.linkTo(divider.start)
                    }
            )

            Spacer(
                modifier = Modifier
                    .constrainAs(divider) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxHeight()
                    .width(1.dp)
            )

            IconTextButton(
                icon = Icons.Filled.EventAvailable,
                text = stringResource(id = R.string.save),
                modifier = Modifier
                    .wrapContentSize()
                    .clickable { onSaveEventClicked() }
                    .constrainAs(saveButton) {
                        start.linkTo(divider.end)
                        end.linkTo(parent.end)
                    }
            )
        }
    }
}