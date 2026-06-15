package cz.cvut.fit.vinyltracker.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cz.cvut.fit.vinyltracker.ui.theme.CharcoalMuted
import cz.cvut.fit.vinyltracker.ui.theme.Cream
import cz.cvut.fit.vinyltracker.ui.theme.Gold

data class FilterPillOption<T>(
    val value: T,
    val label: String,
)

@Composable
fun <T> FilterPillRow(
    options: List<FilterPillOption<T>>,
    selected: T?,
    direction: SortDirection,
    onSelect: (T, SortDirection) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(options, key = { it.label }) { option ->
            val isSelected = option.value == selected
            FilterPill(
                label = option.label,
                selected = isSelected,
                direction = direction,
                onClick = {
                    val newDirection = if (isSelected && direction == SortDirection.ASCENDING)
                        SortDirection.DESCENDING else SortDirection.ASCENDING
                    onSelect(option.value, newDirection)
                },
            )
        }
    }
}

private val PillShape = RoundedCornerShape(50)

@Composable
private fun FilterPill(
    label: String,
    selected: Boolean,
    direction: SortDirection,
    onClick: () -> Unit,
) {
    val borderColor = if (selected) Gold else CharcoalMuted
    val textColor = if (selected) Gold else Cream

    Surface(
        color = androidx.compose.ui.graphics.Color.Transparent,
        modifier = Modifier
            .clip(PillShape)
            .border(width = 1.dp, color = borderColor, shape = PillShape)
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (selected) {
                Icon(
                    imageVector = if (direction == SortDirection.ASCENDING)
                        Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                    contentDescription = null,
                    tint = Gold,
                    modifier = Modifier.size(14.dp),
                )
                Spacer(Modifier.width(4.dp))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = textColor,
            )
        }
    }
}
