package org.goodexpert.ridefit.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import org.goodexpert.ridefit.ui.theme.RideFitTheme
import org.goodexpert.ridefit.ui.theme.rideFitColors

@Composable
fun VoiceScriptCard(
    script: String,
    label: String,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = true,
) {
    val appColors = MaterialTheme.rideFitColors

    val bgColor = appColors.cardContainer
    val borderColor = appColors.brandAccent.copy(alpha = 0.25f)
    val scriptColor = appColors.brandAccent

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (isPlaying) {
                    VoiceWave(color = appColors.brand)
                }
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleSmall,
                    color = appColors.brand,
                )
            }
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = script,
                style = MaterialTheme.typography.bodySmall,
                color = scriptColor,
            )
        }
    }
}

/** A label + script pair (and playing state) for previewing [VoiceScriptCard]. */
data class VoiceScriptSample(
    val label: String,
    val script: String,
    val isPlaying: Boolean,
)

class VoiceScriptCardProvider : PreviewParameterProvider<VoiceScriptSample> {
    override val values = sequenceOf(
        VoiceScriptSample(
            label = "탑승 안내",
            script = "안녕하세요, 안전벨트를 착용해 주세요. 편안하게 모시겠습니다.",
            isPlaying = true,
        ),
        VoiceScriptSample(
            label = "계좌 안내",
            script = "카카오뱅크 3333-04-1234567 홍길동 님께 이체 부탁드립니다.",
            isPlaying = false,
        ),
    )
}

@Preview(name = "VoiceScriptCard · Day", showBackground = true, widthDp = 360)
@Composable
private fun VoiceScriptCardDayPreview(
    @PreviewParameter(VoiceScriptCardProvider::class) sample: VoiceScriptSample,
) {
    RideFitTheme(darkTheme = false) {
        VoiceScriptCard(
            script = sample.script,
            label = sample.label,
            isPlaying = sample.isPlaying,
        )
    }
}

@Preview(name = "VoiceScriptCard · Night", showBackground = true, widthDp = 360, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun VoiceScriptCardNightPreview(
    @PreviewParameter(VoiceScriptCardProvider::class) sample: VoiceScriptSample,
) {
    RideFitTheme(darkTheme = true) {
        VoiceScriptCard(
            script = sample.script,
            label = sample.label,
            isPlaying = sample.isPlaying,
        )
    }
}
