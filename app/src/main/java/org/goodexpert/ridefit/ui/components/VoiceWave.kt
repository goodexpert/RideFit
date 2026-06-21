package org.goodexpert.ridefit.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val waveHeights = listOf(4.dp, 13.dp, 20.dp, 15.dp, 9.dp, 17.dp)
private val waveDelays = listOf(0, 80, 160, 100, 40, 120)

@Composable
fun VoiceWave(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Row(
        modifier = modifier.height(24.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        waveHeights.forEachIndexed { i, h ->
            WaveBar(baseHeight = h, color = color, delayMs = waveDelays[i])
        }
    }
}

@Composable
private fun WaveBar(
    baseHeight: Dp,
    color: Color,
    delayMs: Int,
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition(label = "waveBar")
    val scale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(delayMs),
        ),
        label = "waveBarScale",
    )
    Box(
        modifier = modifier
            .width(4.dp)
            .height(baseHeight * scale)
            .background(color, RoundedCornerShape(2.dp)),
    )
}
