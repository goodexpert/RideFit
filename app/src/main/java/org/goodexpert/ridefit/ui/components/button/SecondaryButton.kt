package org.goodexpert.ridefit.ui.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * A large, tonal secondary action button — same layout as [PrimaryButton] but
 * driven by a caller-supplied tonal [containerColor]/[contentColor] pair and an
 * optional [border]. Use it for confirm/success actions (soft-green tone) or
 * subdued secondary actions (neutral/outlined tone). A preset over [ButtonContent].
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
 * @param horizontalAlignment How the icon(s) and text are aligned as a group;
 *                         defaults to [Alignment.CenterHorizontally].
 * @param leadingIcon      Optional composable shown before the text.
 * @param trailingIcon     Optional composable shown after the text.
 * @param containerColor   Tonal background colour.
 * @param contentColor     Icon and text colour.
 * @param border           Optional outline; use for subdued/neutral variants.
 */
@Composable
fun SecondaryButton(
    title: String,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    subtitle: String? = null,
    enabled: Boolean = true,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    border: BorderStroke? = null,
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
    border = border,
)
