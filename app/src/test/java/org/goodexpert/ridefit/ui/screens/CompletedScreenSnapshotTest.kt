package org.goodexpert.ridefit.ui.screens

import com.github.takahirom.roborazzi.captureRoboImage
import org.goodexpert.ridefit.ui.theme.RideFitTheme
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w360dp-h800dp-port-xxhdpi")
class CompletedScreenSnapshotTest {

    @Test
    fun completed_day() {
        captureRoboImage("src/test/snapshots/completed_day.png") {
            RideFitTheme(darkTheme = false) { CompletedScreen() }
        }
    }

    @Test
    fun completed_night() {
        captureRoboImage("src/test/snapshots/completed_night.png") {
            RideFitTheme(darkTheme = true) { CompletedScreen() }
        }
    }
}
