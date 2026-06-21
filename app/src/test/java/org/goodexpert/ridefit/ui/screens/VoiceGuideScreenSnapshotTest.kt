package org.goodexpert.ridefit.ui.screens

import com.github.takahirom.roborazzi.captureRoboImage
import org.goodexpert.ridefit.model.DriveMode
import org.goodexpert.ridefit.ui.theme.RideFitTheme
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w360dp-h800dp-port-xxhdpi")
class VoiceGuideScreenSnapshotTest {

    @Test
    fun voice_guide_day() {
        captureRoboImage("src/test/snapshots/voice_guide_day.png") {
            RideFitTheme(darkTheme = false) { VoiceGuideScreen(selectedMode = DriveMode.FAST) }
        }
    }

    @Test
    fun voice_guide_night() {
        captureRoboImage("src/test/snapshots/voice_guide_night.png") {
            RideFitTheme(darkTheme = true) { VoiceGuideScreen(selectedMode = DriveMode.SAFE) }
        }
    }
}
