package cz.cvut.fit.vinyltracker.ui.feature.detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.cvut.fit.vinyltracker.ui.theme.Teal

@Composable
fun ButtonMove(
    onMoveToCollection: () -> Unit,
    description: String
) {
    OutlinedButton(
        onClick = onMoveToCollection,
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, Teal),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Teal),
    ) {
        Icon(
            imageVector = Icons.Default.SwapHoriz,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
        )
        Spacer(Modifier.width(8.dp))
        Text(description)
    }
}