package com.example.diaryapp.presentation.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Device
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun DisplayAlertDialog(
    title: String,
    message: String,
    dialogOpened: Boolean,
    onDialogClosed: () -> Unit,
    onYesClicked: () -> Unit,
) {
    if (dialogOpened) {
        AlertDialog(
            title = {
                Text(
                    text = title,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = message,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Normal
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onYesClicked()
                        onDialogClosed()
                    })
                {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = onDialogClosed)
                {
                    Text(text = "No")
                }
            },
            onDismissRequest = onDialogClosed
        )
    }
}

@Preview(showBackground = true, device = Devices.NEXUS_5)
@Composable
fun AlertShowing() {
    DisplayAlertDialog("wahdana","bismillah",true, onDialogClosed = {}, onYesClicked = {})
}