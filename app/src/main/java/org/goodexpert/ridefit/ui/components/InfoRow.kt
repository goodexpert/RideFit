package org.goodexpert.ridefit.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.goodexpert.ridefit.ui.theme.RideFitTheme
import org.goodexpert.ridefit.ui.theme.rideFitColors

@Composable
fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = Color.Unspecified,
    showDivider: Boolean = true,
) {
    val dividerColor = MaterialTheme.rideFitColors.cardBorder
    val effectiveValueColor = if (valueColor == Color.Unspecified) MaterialTheme.colorScheme.onSurface else valueColor

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 9.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
                color = MaterialTheme.rideFitColors.onBackgroundSecondary,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = effectiveValueColor,
            )
        }
        if (showDivider) {
            HorizontalDivider(
                thickness = 0.5.dp,
                color = dividerColor,
            )
        }
    }
}

/** A label/value pair for previewing [InfoRow]. */
data class InfoRowSample(val label: String, val value: String)

class InfoRowProvider : PreviewParameterProvider<InfoRowSample> {
    override val values = sequenceOf(
        InfoRowSample("은행", "카카오뱅크"),
        InfoRowSample("예금주", "홍길동"),
        InfoRowSample("계좌번호", "3333-04-1234567"),
    )
}

@Preview(name = "InfoRow · Day", showBackground = true, widthDp = 320)
@Composable
private fun InfoRowDayPreview(
    @PreviewParameter(InfoRowProvider::class) sample: InfoRowSample,
) {
    RideFitTheme(darkTheme = false) {
        InfoRow(label = sample.label, value = sample.value)
    }
}

@Preview(name = "InfoRow · Night", showBackground = true, widthDp = 320, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun InfoRowNightPreview(
    @PreviewParameter(InfoRowProvider::class) sample: InfoRowSample,
) {
    RideFitTheme(darkTheme = true) {
        InfoRow(label = sample.label, value = sample.value)
    }
}
