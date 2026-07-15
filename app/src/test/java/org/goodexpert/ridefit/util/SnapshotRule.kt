package org.goodexpert.ridefit.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage

/**
 * Tuning for a snapshot capture. Provide sensible defaults at the [SnapshotRule] level
 * and override per call via [SnapshotRule.capture].
 *
 * @param frozenFrameMillis Fixed clock advance applied before capture so animated frames
 *   are deterministic. Freezing the clock is what stops infinite animations (VoiceWave,
 *   PulseDot) from keeping the Compose looper busy and spinning the capture forever.
 * @param roborazziOptions Roborazzi record/compare options; `null` uses Roborazzi's
 *   ambient context options (the default record/verify behaviour).
 */
data class SnapshotConfig(
    val frozenFrameMillis: Long = 300L,
    val roborazziOptions: RoborazziOptions? = null,
)

/**
 * A [ComposeContentTestRule] (usable directly as a JUnit `@get:Rule`) that captures
 * deterministic Roborazzi snapshots with the main clock frozen.
 *
 * Extensible in three ways:
 * - **Rule-level default**: pass a [SnapshotConfig] to the constructor.
 * - **Per-call override**: pass a [SnapshotConfig] to [capture].
 * - **Subclassing**: the class and [capture] are `open`.
 *
 * The compose rule itself is provided by delegation, so `setContent`, `mainClock`,
 * `onRoot()` and the rest of [ComposeContentTestRule] work as usual.
 */
open class SnapshotRule(
    val defaultConfig: SnapshotConfig = SnapshotConfig(),
    private val delegate: ComposeContentTestRule = createComposeRule(),
) : ComposeContentTestRule by delegate {

    open fun capture(
        filePath: String,
        config: SnapshotConfig = defaultConfig,
        content: @Composable () -> Unit,
    ) {
        mainClock.autoAdvance = false
        setContent(content)
        mainClock.advanceTimeBy(config.frozenFrameMillis)
        val root = onRoot()
        val options = config.roborazziOptions
        if (options != null) root.captureRoboImage(filePath, options) else root.captureRoboImage(filePath)
    }
}
