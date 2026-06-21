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
class AccountSettingsScreenSnapshotTest {

    @Test
    fun settings_filled_day() {
        captureRoboImage("src/test/snapshots/settings_filled_day.png") {
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
        captureRoboImage("src/test/snapshots/settings_filled_night.png") {
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
        captureRoboImage("src/test/snapshots/settings_empty_day.png") {
            RideFitTheme(darkTheme = false) { AccountSettingsScreen() }
        }
    }
}
