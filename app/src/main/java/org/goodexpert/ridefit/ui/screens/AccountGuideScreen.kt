package org.goodexpert.ridefit.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.goodexpert.ridefit.R
import org.goodexpert.ridefit.model.BankAccount
import org.goodexpert.ridefit.ui.components.InfoRow
import org.goodexpert.ridefit.ui.components.StatusBadge
import org.goodexpert.ridefit.ui.components.VoiceScriptCard
import org.goodexpert.ridefit.ui.theme.RideFitColors
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
    val colorScheme = MaterialTheme.colorScheme
    val appColors = MaterialTheme.rideFitColors

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .safeDrawingPadding()
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
            AccountInfoCard(bankAccount = bankAccount, appColors = appColors)
            RepeatButton(onClick = onRepeat)
            ConfirmButton(onClick = onConfirm)
        } else {
            NoAccountCard(appColors = appColors, onSetupAccount = onSetupAccount)
        }
    }
}

// ── Header ────────────────────────────────────────────────────────────────────

@Composable
private fun AccountGuideHeader() {
    val colorScheme = MaterialTheme.colorScheme

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        StatusBadge(text = stringResource(R.string.transfer_badge))
        Text(
            text = stringResource(R.string.transfer_title),
            style = MaterialTheme.typography.headlineLarge,
            color = colorScheme.onBackground,
        )
        Text(
            text = stringResource(R.string.transfer_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = colorScheme.onBackground.copy(alpha = 0.55f),
        )
    }
}

// ── Account info card ─────────────────────────────────────────────────────────

@Composable
private fun AccountInfoCard(
    bankAccount: BankAccount,
    appColors: RideFitColors,
    modifier: Modifier = Modifier,
) {
    val isDark = appColors.isDark
    val bgColor = if (!isDark) MaterialTheme.colorScheme.surface else Color(0xFF14142A)
    val borderColor = if (!isDark) Color(0xFFD0DEFF) else Color(0xFF2A2A4A)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.dp, borderColor),
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
                valueColor = appColors.brand,
                showDivider = false,
            )
        }
    }
}

// ── Repeat button ─────────────────────────────────────────────────────────────

@Composable
private fun RepeatButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    val appColors = MaterialTheme.rideFitColors
    val isDark = appColors.isDark

    val bgColor = if (!isDark) Color(0xFFF5F7FF) else Color(0xFF14142A)
    val borderColor = if (!isDark) Color(0xFFD0DEFF) else Color(0xFF2A2A4A)

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Repeat,
                contentDescription = null,
                tint = appColors.brand,
                modifier = Modifier.size(22.dp),
            )
            Column {
                Text(
                    text = stringResource(R.string.transfer_repeat_label),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = colorScheme.onSurface,
                )
                Text(
                    text = stringResource(R.string.transfer_repeat_sub),
                    style = MaterialTheme.typography.labelSmall,
                    color = appColors.onBackgroundSecondary,
                )
            }
        }
    }
}

// ── Confirm button ────────────────────────────────────────────────────────────

@Composable
private fun ConfirmButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDark = MaterialTheme.rideFitColors.isDark

    val bgColor = if (!isDark) Color(0xFFDCFCE7) else Color(0xFF052E16)
    val contentColor = if (!isDark) Color(0xFF15803D) else Color(0xFF4ADE80)

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(28.dp),
            )
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = stringResource(R.string.transfer_confirm),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp,
                ),
                color = contentColor,
            )
        }
    }
}

// ── No account state ──────────────────────────────────────────────────────────

@Composable
private fun NoAccountCard(
    appColors: RideFitColors,
    onSetupAccount: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDark = appColors.isDark
    val bgColor = if (!isDark) Color(0xFFFFF9EC) else Color(0xFF1A1400)
    val borderColor = if (!isDark) Color(0xFFFFE4A0) else Color(0xFF3A2800)
    val titleColor = if (!isDark) Color(0xFF92400E) else Color(0xFFFBBF24)
    val subColor = if (!isDark) Color(0xFFA85C10) else Color(0xFF9A7010)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = bgColor),
            border = BorderStroke(1.dp, borderColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = stringResource(R.string.transfer_no_account),
                    style = MaterialTheme.typography.titleMedium,
                    color = titleColor,
                )
                Text(
                    text = stringResource(R.string.transfer_no_account_desc),
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
                    color = subColor,
                )
            }
        }
        Card(
            onClick = onSetupAccount,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 80.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = appColors.brand),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp),
                )
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = stringResource(R.string.transfer_setup),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 22.sp,
                    ),
                    color = Color.White,
                )
            }
        }
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

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
