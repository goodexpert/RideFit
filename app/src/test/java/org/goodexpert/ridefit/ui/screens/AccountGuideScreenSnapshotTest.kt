package org.goodexpert.ridefit.ui.screens

import com.github.takahirom.roborazzi.captureRoboImage
import org.goodexpert.ridefit.model.BankAccount
import org.goodexpert.ridefit.ui.theme.RideFitTheme
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w360dp-h800dp-port-xxhdpi")
class AccountGuideScreenSnapshotTest {

    @Test
    fun accountGuide_withAccount_day() {
        captureRoboImage("src/test/snapshots/accountGuide_withAccount_day.png") {
            RideFitTheme(darkTheme = false) {
                AccountGuideScreen(
                    bankAccount = BankAccount(
                        bankName = "카카오뱅크",
                        holderName = "홍길동",
                        accountNumber = "3333-0448-7729-1234",
                    ),
                )
            }
        }
    }

    @Test
    fun accountGuide_withAccount_night() {
        captureRoboImage("src/test/snapshots/accountGuide_withAccount_night.png") {
            RideFitTheme(darkTheme = true) {
                AccountGuideScreen(
                    bankAccount = BankAccount(
                        bankName = "카카오뱅크",
                        holderName = "홍길동",
                        accountNumber = "3333-0448-7729-1234",
                    ),
                )
            }
        }
    }

    @Test
    fun accountGuide_noAccount_day() {
        captureRoboImage("src/test/snapshots/accountGuide_noAccount_day.png") {
            RideFitTheme(darkTheme = false) { AccountGuideScreen(bankAccount = BankAccount()) }
        }
    }
}
