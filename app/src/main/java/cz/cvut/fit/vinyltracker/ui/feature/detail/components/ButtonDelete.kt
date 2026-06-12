package cz.cvut.fit.vinyltracker.ui.feature.detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.cvut.fit.vinyltracker.R
import cz.cvut.fit.vinyltracker.ui.theme.ErrorRed

@Composable
fun ButtonDelete(
    description: String,
    onDelete: () -> Unit
) {
    OutlinedButton(
        onClick = onDelete,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        border = BorderStroke(1.dp, ErrorRed),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = description,
            modifier = Modifier.size(18.dp),
        )
        Spacer(Modifier.width(8.dp))
        Text(description)
    }
}