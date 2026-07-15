package org.goodexpert.ridefit.ui.screens

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
class CompletedScreenSnapshotTest {

    @get:Rule
    val snapshotRule = SnapshotRule()

    @Test
    fun completed_day() {
        snapshotRule.capture("src/test/snapshots/completed_day.png") {
            RideFitTheme(darkTheme = false) { CompletedScreen() }
        }
    }

    @Test
    fun completed_night() {
        snapshotRule.capture("src/test/snapshots/completed_night.png") {
            RideFitTheme(darkTheme = true) { CompletedScreen() }
        }
    }
}
