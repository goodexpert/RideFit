package org.goodexpert.ridefit.ui.components

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
class BankPickerBottomSheetSnapshotTest {

    @Test
    fun bankPicker_day() {
        captureRoboImage("src/test/snapshots/bank_picker_day.png") {
            RideFitTheme(darkTheme = false) {
                BankPickerBottomSheet(
                    onDismiss = {},
                    onBankSelect = {},
                )
            }
        }
    }

    @Test
    fun bankPicker_night() {
        captureRoboImage("src/test/snapshots/bank_picker_night.png") {
            RideFitTheme(darkTheme = true) {
                BankPickerBottomSheet(
                    onDismiss = {},
                    onBankSelect = {},
                )
            }
        }
    }
}
