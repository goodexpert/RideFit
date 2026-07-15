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
class AccountSettingsScreenSnapshotTest {

    @get:Rule
    val snapshotRule = SnapshotRule()

    @Test
    fun settings_filled_day() {
        snapshotRule.capture("src/test/snapshots/settings_filled_day.png") {
            RideFitTheme(darkTheme = false) {
                AccountSettingsScreen(
                    bankName = "카카오뱅크",
                    holderName = "홍길동",
                    accountNumber = "3333-0448-7729-1234",
                )
            }
        }
    }

    @Test
    fun settings_filled_night() {
        snapshotRule.capture("src/test/snapshots/settings_filled_night.png") {
            RideFitTheme(darkTheme = true) {
                AccountSettingsScreen(
                    bankName = "카카오뱅크",
                    holderName = "홍길동",
                    accountNumber = "3333-0448-7729-1234",
                )
            }
        }
    }

    @Test
    fun settings_empty_day() {
        snapshotRule.capture("src/test/snapshots/settings_empty_day.png") {
            RideFitTheme(darkTheme = false) { AccountSettingsScreen() }
        }
    }
}
