package org.goodexpert.ridefit.ui.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/**
 * Shared implementation for RideFit's large action buttons — a clickable [Surface]
 * (flat, no elevation) wrapping optional leading/trailing icon slots, a bold title
 * and an optional subtitle line.
 *
 * [PrimaryButton] and [SecondaryButton] are thin presets over this; callers should
 * use those rather than this internal function.
 *
 * Disabled colours are derived here (Surface has no built-in disabled states), and
 * [contentColor] is propagated to the content via `LocalContentColor`.
 *
 * @param titleColor    Colour for the title; defaults to the ambient content colour.
 * @param subtitleColor Colour for the subtitle; defaults to a dimmed content colour.
 */
@Composable
internal fun ButtonContent(
    title: String,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    subtitle: String? = null,
    enabled: Boolean = true,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    border: BorderStroke? = null,
    titleColor: Color? = null,
    subtitleColor: Color? = null,
) {
    val semanticsModifier = if (!contentDescription.isNullOrBlank()) {
        Modifier.semantics { this.contentDescription = contentDescription }
    } else {
        Modifier
    }

    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .then(semanticsModifier),
        shape = RoundedCornerShape(16.dp),
        color = if (enabled) containerColor else containerColor.copy(alpha = 0.4f),
        contentColor = if (enabled) contentColor else contentColor.copy(alpha = 0.6f),
        border = border,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp, horizontalAlignment),
        ) {
            leadingIcon?.invoke()
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = titleColor ?: LocalContentColor.current,
                )
                if (!subtitle.isNullOrBlank()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = subtitleColor ?: LocalContentColor.current.copy(alpha = 0.65f),
                    )
                }
            }
            trailingIcon?.invoke()
        }
    }
}
