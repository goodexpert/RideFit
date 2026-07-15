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
class MainScreenSnapshotTest {

    @get:Rule
    val snapshotRule = SnapshotRule()

    @Test
    fun main_standby_day() = snapshotRule.capture("src/test/snapshots/main_standby_day.png") {
        RideFitTheme(darkTheme = false) { MainScreen(selectedMode = null) }
    }

    @Test
    fun main_standby_night() = snapshotRule.capture("src/test/snapshots/main_standby_night.png") {
        RideFitTheme(darkTheme = true) { MainScreen(selectedMode = null) }
    }

    @Test
    fun main_riding_day() = snapshotRule.capture("src/test/snapshots/main_riding_day.png") {
        RideFitTheme(darkTheme = false) {
            MainScreen(isRiding = true, selectedMode = DriveMode.QUIET)
        }
    }

    @Test
    fun main_riding_night() = snapshotRule.capture("src/test/snapshots/main_riding_night.png") {
        RideFitTheme(darkTheme = true) {
            MainScreen(isRiding = true, selectedMode = DriveMode.QUIET)
        }
    }
}
