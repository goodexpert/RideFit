package org.goodexpert.ridefit.ui.screens

import org.goodexpert.ridefit.model.DriveMode
import org.goodexpert.ridefit.ui.theme.RideFitTheme
import org.goodexpert.ridefit.util.SnapshotRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w360dp-h800dp-port-xxhdpi")
class VoiceGuideScreenSnapshotTest {

    @get:Rule
    val snapshotRule = SnapshotRule()

    @Test
    fun voice_guide_day() = snapshotRule.capture("src/test/snapshots/voice_guide_day.png") {
        RideFitTheme(darkTheme = false) { VoiceGuideScreen(selectedMode = DriveMode.FAST) }
    }

    @Test
    fun voice_guide_night() = snapshotRule.capture("src/test/snapshots/voice_guide_night.png") {
        RideFitTheme(darkTheme = true) { VoiceGuideScreen(selectedMode = DriveMode.SAFE) }
    }
}
