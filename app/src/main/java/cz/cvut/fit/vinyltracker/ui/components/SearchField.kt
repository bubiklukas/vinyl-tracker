package cz.cvut.fit.vinyltracker.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.cvut.fit.vinyltracker.R
import cz.cvut.fit.vinyltracker.ui.theme.CharcoalMuted
import cz.cvut.fit.vinyltracker.ui.theme.Cream
import cz.cvut.fit.vinyltracker.ui.theme.SurfaceHigh

@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(placeholder ?: stringResource(R.string.search_placeholder), color = CharcoalMuted) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = CharcoalMuted) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = SurfaceHigh,
            unfocusedContainerColor = SurfaceHigh,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedTextColor = Cream,
            unfocusedTextColor = Cream,
        ),
    )
}
