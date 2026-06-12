package cz.cvut.fit.vinyltracker.ui.feature.detail.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cz.cvut.fit.vinyltracker.R
import cz.cvut.fit.vinyltracker.ui.theme.CharcoalMuted
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun OwnedSinceText(ownedSince: LocalDateTime?) {
    if (ownedSince == null) return
    Text(
        text = stringResource(
            R.string.detail_owned_since,
            ownedSince.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")),
        ),
        style = MaterialTheme.typography.bodySmall,
        color = CharcoalMuted,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 20.dp),
    )
}
