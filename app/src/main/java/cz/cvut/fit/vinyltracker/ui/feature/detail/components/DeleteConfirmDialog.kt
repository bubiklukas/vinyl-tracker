package cz.cvut.fit.vinyltracker.ui.feature.detail.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cz.cvut.fit.vinyltracker.R
import cz.cvut.fit.vinyltracker.ui.theme.Cream
import cz.cvut.fit.vinyltracker.ui.theme.ErrorRed

@Composable
fun DeleteConfirmDialog(
    vinylTitle: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.detail_delete_confirm_title),
                style = MaterialTheme.typography.titleMedium,
                color = Cream,
            )
        },
        text = {
            Text(
                text = stringResource(R.string.detail_delete_confirm_text, vinylTitle),
                color = Cream,
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.detail_delete_confirm_yes), color = ErrorRed)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.detail_delete_confirm_no), color = Cream)
            }
        },
    )
}
