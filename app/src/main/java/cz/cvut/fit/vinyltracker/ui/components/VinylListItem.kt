package cz.cvut.fit.vinyltracker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import cz.cvut.fit.vinyltracker.R
import cz.cvut.fit.vinyltracker.domain.Vinyl
import cz.cvut.fit.vinyltracker.ui.theme.CharcoalMuted
import cz.cvut.fit.vinyltracker.ui.theme.Cream
import cz.cvut.fit.vinyltracker.ui.theme.Gold
import cz.cvut.fit.vinyltracker.ui.theme.Teal
import cz.cvut.fit.vinyltracker.ui.theme.WarmMuted

@Composable
fun VinylListItem(
    vinyl: Vinyl,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showBadge: Boolean = true,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = vinyl.coverUrl,
            contentDescription = vinyl.title,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
        )

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = vinyl.title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = Cream,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = vinyl.artist,
                style = MaterialTheme.typography.bodySmall,
                color = WarmMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (vinyl.year != null) {
                Text(
                    text = vinyl.year.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = CharcoalMuted,
                )
            }
        }

        if (showBadge) {
            Text(
                text = stringResource(if (vinyl.owned) R.string.badge_collection else R.string.badge_wishlist),
                style = MaterialTheme.typography.labelSmall,
                color = if (vinyl.owned) Gold else Teal,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = stringResource(R.string.cd_navigate_to_detail),
            tint = CharcoalMuted,
        )
    }
}
