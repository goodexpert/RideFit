package org.goodexpert.ridefit.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.goodexpert.ridefit.R
import org.goodexpert.ridefit.model.BankAccount
import org.goodexpert.ridefit.ui.components.BankPickerBottomSheet
import org.goodexpert.ridefit.ui.components.VoiceScriptCard
import org.goodexpert.ridefit.ui.theme.RideFitTheme
import org.goodexpert.ridefit.ui.theme.rideFitColors

@Composable
fun AccountSettingsScreen(
    modifier: Modifier = Modifier,
    bankName: String = "",
    holderName: String = "",
    accountNumber: String = "",
    isDirty: Boolean = false,
    onBankPickerOpen: () -> Unit = {},
    onBankNameChange: (String) -> Unit = {},
    onHolderNameChange: (String) -> Unit = {},
    onAccountNumberChange: (String) -> Unit = {},
    onSave: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    var showBankPicker by remember { mutableStateOf(false) }
    var showDiscardDialog by remember { mutableStateOf(false) }
    val colorScheme = MaterialTheme.colorScheme

    val handleBack = { if (isDirty) showDiscardDialog = true else onBack() }

    BackHandler(enabled = true) { handleBack() }

    if (showDiscardDialog) {
        AlertDialog(
            onDismissRequest = { showDiscardDialog = false },
            title = { Text(stringResource(R.string.discard_changes_dialog_title)) },
            text = { Text(stringResource(R.string.discard_changes_dialog_message)) },
            confirmButton = {
                TextButton(onClick = {
                    showDiscardDialog = false
                    onBack()
                }) {
                    Text(stringResource(R.string.discard_changes_dialog_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDiscardDialog = false }) {
                    Text(stringResource(R.string.discard_changes_dialog_cancel))
                }
            },
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .safeDrawingPadding(),
    ) {
        SettingsTopBar(onBack = handleBack)
        HorizontalDivider(
            thickness = 0.5.dp,
            color = colorScheme.onBackground.copy(alpha = 0.08f),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            SettingsHeader()
            SettingsFields(
                bankName = bankName,
                holderName = holderName,
                accountNumber = accountNumber,
                onBankFieldClick = {
                    onBankPickerOpen()
                    showBankPicker = true
                },
                onHolderNameChange = onHolderNameChange,
                onAccountNumberChange = onAccountNumberChange,
                onSave = onSave,
            )
            VoicePreviewSection(
                bankName = bankName,
                holderName = holderName,
                accountNumber = accountNumber,
            )
            SaveButton(onClick = onSave)
        }
    }

    if (showBankPicker) {
        BankPickerBottomSheet(
            onDismiss = { showBankPicker = false },
            onBankSelect = { name ->
                onBankNameChange(name)
                showBankPicker = false
            },
        )
    }
}

// ── Top bar ───────────────────────────────────────────────────────────────────

@Composable
private fun SettingsTopBar(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "뒤로",
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
        Text(
            text = stringResource(R.string.settings_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

// ── Header ────────────────────────────────────────────────────────────────────

@Composable
private fun SettingsHeader(modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = stringResource(R.string.settings_header),
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 24.sp),
            color = colorScheme.onBackground,
        )
        Text(
            text = stringResource(R.string.settings_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = colorScheme.onBackground.copy(alpha = 0.55f),
        )
    }
}

// ── Input fields ──────────────────────────────────────────────────────────────

@Composable
private fun SettingsFields(
    bankName: String,
    holderName: String,
    accountNumber: String,
    onHolderNameChange: (String) -> Unit,
    onAccountNumberChange: (String) -> Unit,
    onSave: () -> Unit,
    onBankFieldClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val holderFocus = remember { FocusRequester() }
    val accountFocus = remember { FocusRequester() }
    val appColors = MaterialTheme.rideFitColors
    val isDark = appColors.isDark
    val unfocusedBorder = if (!isDark) Color(0xFFD0DEFF) else Color(0xFF2A2A4A)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        BankNameField(
            label = stringResource(R.string.settings_bank_name),
            value = bankName,
            placeholder = stringResource(R.string.settings_bank_name_hint),
            unfocusedBorderColor = unfocusedBorder,
            onClick = onBankFieldClick,
        )
        AccountField(
            label = stringResource(R.string.settings_holder_name),
            value = holderName,
            placeholder = stringResource(R.string.settings_holder_name_hint),
            onValueChange = onHolderNameChange,
            unfocusedBorderColor = unfocusedBorder,
            imeAction = ImeAction.Next,
            keyboardActions = KeyboardActions(onNext = { accountFocus.requestFocus() }),
            fieldModifier = Modifier.focusRequester(holderFocus),
        )
        AccountField(
            label = stringResource(R.string.settings_account_number),
            value = accountNumber,
            placeholder = stringResource(R.string.settings_account_number_hint),
            onValueChange = onAccountNumberChange,
            unfocusedBorderColor = unfocusedBorder,
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(onDone = { onSave() }),
            fieldModifier = Modifier.focusRequester(accountFocus),
        )
    }
}

@Composable
private fun AccountField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    unfocusedBorderColor: Color,
    imeAction: ImeAction,
    keyboardActions: KeyboardActions,
    modifier: Modifier = Modifier,
    fieldModifier: Modifier = Modifier,
) {
    val appColors = MaterialTheme.rideFitColors

    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            ),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 6.dp),
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .then(fieldModifier),
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.35f),
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = appColors.brand,
                unfocusedBorderColor = unfocusedBorderColor,
                focusedLabelColor = appColors.brand,
                unfocusedLabelColor = appColors.onBackgroundSecondary,
            ),
            keyboardOptions = KeyboardOptions(imeAction = imeAction),
            keyboardActions = keyboardActions,
        )
    }
}

// ── Voice preview ─────────────────────────────────────────────────────────────

@Composable
private fun VoicePreviewSection(
    bankName: String,
    holderName: String,
    accountNumber: String,
    modifier: Modifier = Modifier,
) {
    val account = BankAccount(bankName, holderName, accountNumber)
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(R.string.settings_voice_preview),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
            ),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        )
        if (account.isConfigured) {
            val preview = stringResource(
                R.string.script_settings_preview,
                bankName,
                holderName,
                account.spokenAccountNumber,
            )
            VoiceScriptCard(
                script = preview,
                label = stringResource(R.string.settings_voice_preview_label),
                isPlaying = false,
            )
        }
    }
}

// ── Save button ───────────────────────────────────────────────────────────────

@Composable
private fun SaveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val appColors = MaterialTheme.rideFitColors

    Card(
        onClick = onClick,
        modifier = modifier
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
                imageVector = Icons.Filled.Save,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(26.dp),
            )
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = stringResource(R.string.settings_save),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp,
                ),
                color = Color.White,
            )
        }
    }
}

// ── Bank name field (read-only, opens picker) ─────────────────────────────────

@Composable
private fun BankNameField(
    label: String,
    value: String,
    placeholder: String,
    unfocusedBorderColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val appColors = MaterialTheme.rideFitColors
    val colorScheme = MaterialTheme.colorScheme

    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            ),
            color = colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 6.dp),
        )
        OutlinedTextField(
            value = value,
            onValueChange = {},
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = unfocusedBorderColor,
                disabledTextColor = colorScheme.onBackground,
                disabledPlaceholderColor = colorScheme.onBackground.copy(alpha = 0.35f),
                disabledTrailingIconColor = appColors.onBackgroundSecondary,
                disabledContainerColor = colorScheme.surface,
                focusedBorderColor = appColors.brand,
                unfocusedBorderColor = unfocusedBorderColor,
            ),
        )
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(name = "Settings · Day", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun AccountSettingsScreenDayPreview() {
    RideFitTheme(darkTheme = false) {
        AccountSettingsScreen(
            bankName = "카카오뱅크",
            holderName = "홍길동",
            accountNumber = "3333-0448-7729-1234",
        )
    }
}

@Preview(name = "Settings · Night", showBackground = true, widthDp = 360, heightDp = 800, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun AccountSettingsScreenNightPreview() {
    RideFitTheme(darkTheme = true) {
        AccountSettingsScreen(
            bankName = "카카오뱅크",
            holderName = "홍길동",
            accountNumber = "3333-0448-7729-1234",
        )
    }
}

@Preview(name = "Settings · Empty", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun AccountSettingsScreenEmptyPreview() {
    RideFitTheme(darkTheme = false) {
        AccountSettingsScreen()
    }
}
