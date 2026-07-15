package org.goodexpert.ridefit.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.goodexpert.ridefit.R
import org.goodexpert.ridefit.model.BankAccount
import org.goodexpert.ridefit.ui.components.BannerAd
import org.goodexpert.ridefit.ui.components.InfoRow
import org.goodexpert.ridefit.ui.components.StatusBadge
import org.goodexpert.ridefit.ui.components.VoiceScriptCard
import org.goodexpert.ridefit.ui.components.button.PrimaryButton
import org.goodexpert.ridefit.ui.components.button.SecondaryButton
import org.goodexpert.ridefit.ui.theme.RideFitTheme
import org.goodexpert.ridefit.ui.theme.rideFitColors

@Composable
fun AccountGuideScreen(
    modifier: Modifier = Modifier,
    bankAccount: BankAccount = BankAccount(),
    onRepeat: () -> Unit = {},
    onConfirm: () -> Unit = {},
    onSetupAccount: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding(),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AccountGuideHeader()

            if (bankAccount.isConfigured) {
                val script = stringResource(
                    R.string.script_transfer,
                    bankAccount.bankName,
                    bankAccount.holderName,
                    bankAccount.spokenAccountNumber,
                )
                VoiceScriptCard(
                    script = script,
                    label = stringResource(R.string.transfer_voice_label),
                )
                AccountInfoCard(bankAccount = bankAccount)
                RepeatButton(onClick = onRepeat)
                ConfirmButton(onClick = onConfirm)
            } else {
                NoAccountCard(onSetupAccount = onSetupAccount)
            }
        }

        // 하단 고정 배너 광고
        BannerAd()
    }
}

@Composable
private fun AccountGuideHeader() {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        StatusBadge(text = stringResource(R.string.transfer_badge))
        Text(
            text = stringResource(R.string.transfer_title),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = stringResource(R.string.transfer_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
        )
    }
}

@Composable
private fun AccountInfoCard(
    bankAccount: BankAccount,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.rideFitColors.cardContainer),
        border = BorderStroke(1.dp, MaterialTheme.rideFitColors.cardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            InfoRow(
                label = stringResource(R.string.transfer_bank_label),
                value = bankAccount.bankName,
            )
            InfoRow(
                label = stringResource(R.string.transfer_holder_label),
                value = bankAccount.holderName,
            )
            InfoRow(
                label = stringResource(R.string.transfer_account_label),
                value = bankAccount.accountNumber,
                valueColor = MaterialTheme.rideFitColors.brand,
                showDivider = false,
            )
        }
    }
}

@Composable
private fun RepeatButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.rideFitColors.subtleContainer,
        border = BorderStroke(1.dp, MaterialTheme.rideFitColors.cardBorder),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Repeat,
                contentDescription = null,
                tint = MaterialTheme.rideFitColors.brand,
                modifier = Modifier.size(24.dp),
            )
            Column {
                Text(
                    text = stringResource(R.string.transfer_repeat_label),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = stringResource(R.string.transfer_repeat_sub),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.rideFitColors.onBackgroundSecondary,
                )
            }
        }
    }
}

@Composable
private fun ConfirmButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SecondaryButton(
        title = stringResource(R.string.transfer_confirm),
        contentDescription = stringResource(R.string.transfer_confirm),
        onClick = onClick,
        modifier = modifier,
        containerColor = MaterialTheme.rideFitColors.confirmContainer,
        contentColor = MaterialTheme.rideFitColors.onConfirmContainer,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
            )
        },
    )
}

@Composable
private fun NoAccountCard(
    onSetupAccount: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.rideFitColors.warningContainer),
            border = BorderStroke(1.dp, MaterialTheme.rideFitColors.warningBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = stringResource(R.string.transfer_no_account),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.rideFitColors.onWarning,
                )
                Text(
                    text = stringResource(R.string.transfer_no_account_desc),
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
                    color = MaterialTheme.rideFitColors.onWarningVariant,
                )
            }
        }
        PrimaryButton(
            title = stringResource(R.string.transfer_setup),
            contentDescription = stringResource(R.string.transfer_setup),
            onClick = onSetupAccount,
            containerColor = MaterialTheme.rideFitColors.brand,
            horizontalAlignment = Alignment.CenterHorizontally,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = null,
                    modifier = Modifier.size(26.dp),
                )
            },
        )
    }
}

private val sampleAccount = BankAccount(
    bankName = "카카오뱅크",
    holderName = "홍길동",
    accountNumber = "3333-0448-7729-1234",
)

@Preview(name = "Transfer · Day", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun AccountGuideScreenDayPreview() {
    RideFitTheme(darkTheme = false) {
        AccountGuideScreen(bankAccount = sampleAccount)
    }
}

@Preview(name = "Transfer · Night", showBackground = true, widthDp = 360, heightDp = 800, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun AccountGuideScreenNightPreview() {
    RideFitTheme(darkTheme = true) {
        AccountGuideScreen(bankAccount = sampleAccount)
    }
}

@Preview(name = "Transfer · No Account", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun AccountGuideScreenNoAccountPreview() {
    RideFitTheme(darkTheme = false) {
        AccountGuideScreen(bankAccount = BankAccount())
    }
}
