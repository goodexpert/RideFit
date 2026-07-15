package org.goodexpert.ridefit.ui.components.button

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.goodexpert.ridefit.ui.theme.rideFitColors

/**
 * A large, solid primary action button — optional leading/trailing icon slots, a
 * bold title and an optional subtitle line. A brand-coloured preset over [ButtonContent].
 *
 * The button wraps its content height — the title/subtitle plus 16dp of vertical
 * padding — rather than enforcing a fixed minimum.
 *
 * @param title            Bold label shown on the button.
 * @param onClick          Invoked when the button is tapped.
 * @param contentDescription Accessibility label announced by screen readers. When
 *                         null, the visible [title] (and [subtitle]) are used instead.
 * @param subtitle         Optional sub-label shown beneath the title.
 * @param enabled          Whether the button responds to input; a disabled button
 *                         is dimmed and ignores clicks.
 * @param horizontalAlignment How the icon(s) and text are aligned as a group —
 *                         [Alignment.Start] (default), [Alignment.CenterHorizontally]
 *                         or [Alignment.End]. Centre suits single-line buttons
 *                         without a [subtitle].
 * @param leadingIcon      Optional composable shown before the text.
 * @param trailingIcon     Optional composable shown after the text.
 * @param containerColor   Background colour; defaults to the brand blue.
 * @param contentColor     Icon and text colour; defaults to white.
 */
@Composable
fun PrimaryButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    subtitle: String? = null,
    enabled: Boolean = true,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    containerColor: Color = MaterialTheme.rideFitColors.brand,
    contentColor: Color = Color.White,
) = ButtonContent(
    title = title,
    onClick = onClick,
    containerColor = containerColor,
    contentColor = contentColor,
    modifier = modifier,
    contentDescription = contentDescription,
    subtitle = subtitle,
    enabled = enabled,
    horizontalAlignment = horizontalAlignment,
    leadingIcon = leadingIcon,
    trailingIcon = trailingIcon,
)
