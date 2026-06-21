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
class MainScreenSnapshotTest {

    @Test
    fun main_standby_day() {
        captureRoboImage("src/test/snapshots/main_standby_day.png") {
            RideFitTheme(darkTheme = false) { MainScreen(selectedMode = null) }
        }
    }

    @Test
    fun main_standby_night() {
        captureRoboImage("src/test/snapshots/main_standby_night.png") {
            RideFitTheme(darkTheme = true) { MainScreen(selectedMode = null) }
        }
    }

    @Test
    fun main_riding_day() {
        captureRoboImage("src/test/snapshots/main_riding_day.png") {
            RideFitTheme(darkTheme = false) {
                MainScreen(isRiding = true, selectedMode = DriveMode.QUIET)
            }
        }
    }

    @Test
    fun main_riding_night() {
        captureRoboImage("src/test/snapshots/main_riding_night.png") {
            RideFitTheme(darkTheme = true) {
                MainScreen(isRiding = true, selectedMode = DriveMode.QUIET)
            }
        }
    }
}
