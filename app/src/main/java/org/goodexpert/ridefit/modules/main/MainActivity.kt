package org.goodexpert.ridefit.modules.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import kotlinx.coroutines.launch
import org.goodexpert.ridefit.R
import org.goodexpert.ridefit.model.BankAccount
import org.goodexpert.ridefit.model.DriveMode
import org.goodexpert.ridefit.modules.account.AccountViewModel
import org.goodexpert.ridefit.modules.main.MainContract.AppScreen
import org.goodexpert.ridefit.repository.EmergencyRecordingService
import org.goodexpert.ridefit.ui.screens.AccountGuideScreen
import org.goodexpert.ridefit.ui.screens.AccountSettingsScreen
import org.goodexpert.ridefit.ui.screens.CompletedScreen
import org.goodexpert.ridefit.ui.screens.EmergencyVideoScreen
import org.goodexpert.ridefit.ui.screens.MainScreen
import org.goodexpert.ridefit.ui.screens.VoiceGuideScreen
import org.goodexpert.ridefit.ui.theme.RideFitTheme
import java.io.File

class MainActivity : ComponentActivity() {

    private val accountViewModel: AccountViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    private val emergencyPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { results ->
        if (results.all { it.value }) {
            EmergencyRecordingService.start(this)
            mainViewModel.onEmergencyRecordingStartedHandler()
        }
    }

    private fun startEmergencyRecording() {
        if (mainViewModel.viewState.value.isRecording) return
        val required = buildList {
            add(Manifest.permission.CAMERA)
            add(Manifest.permission.RECORD_AUDIO)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }.toTypedArray()
        if (required.all { checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }) {
            EmergencyRecordingService.start(this)
            mainViewModel.onEmergencyRecordingStartedHandler()
        } else {
            emergencyPermissionLauncher.launch(required)
        }
    }

    private fun stopEmergencyRecording() {
        EmergencyRecordingService.stop(this)
    }

    private fun playVideo(file: File) {
        val uri = runCatching {
            FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
        }.getOrNull() ?: return
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "video/mp4")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        runCatching { startActivity(intent) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewState by mainViewModel.viewState.collectAsState()
            val systemDark = isSystemInDarkTheme()
            RideFitTheme(darkTheme = if (viewState.isDarkMode) true else systemDark) {
                AppContent()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun AppContent() {
        val viewState by mainViewModel.viewState.collectAsState()
        val accountViewState by accountViewModel.viewState.collectAsState()
        val bankAccount = accountViewState.bankAccount
        val draft = accountViewState.draft
        val isDirty = accountViewState.isDirty

        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val scope = rememberCoroutineScope()

        BackHandler { mainViewModel.onBackHandler() }

        LaunchedEffect(Unit) {
            mainViewModel.sideEffect.collect { effect ->
                when (effect) {
                    MainContract.SideEffect.Finish -> finish()
                    MainContract.SideEffect.StartEmergencyRecording -> startEmergencyRecording()
                    MainContract.SideEffect.StopEmergencyRecording -> stopEmergencyRecording()
                    is MainContract.SideEffect.PlayVideo -> playVideo(effect.file)
                    MainContract.SideEffect.HideSettings -> {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            mainViewModel.onSheetClosedHandler()
                        }
                    }
                    is MainContract.SideEffect.HideSettingsThenNavigate -> {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            mainViewModel.onSheetNavigatedHandler(effect.destination)
                        }
                    }
                }
            }
        }

        // ── Dialogs ───────────────────────────────────────────────────────────

        AppDialogs(
            showCancelGuide = viewState.showCancelGuideDialog,
            showExit = viewState.showExitDialog,
            showStopRecording = viewState.showStopRecordingDialog,
            onCancelGuideConfirm = mainViewModel::onCancelGuideConfirmHandler,
            onCancelGuideDismiss = mainViewModel::onCancelGuideDismissHandler,
            onExitConfirm = mainViewModel::onExitConfirmHandler,
            onExitDismiss = mainViewModel::onExitDismissHandler,
            onStopRecordingConfirm = mainViewModel::onStopRecordingConfirmHandler,
            onStopRecordingDismiss = mainViewModel::onStopRecordingDismissHandler,
        )

        // ── Screens + overlays ────────────────────────────────────────────────

        AppScreenContent(
            viewState = viewState,
            draft = draft,
            isDirty = isDirty,
            bankAccount = bankAccount,
            onBack = mainViewModel::onBackHandler,
            onModeChange = mainViewModel::onModeSelectedHandler,
            onOpenSettings = mainViewModel::onOpenSettingsHandler,
            onStartGuide = mainViewModel::onStartGuideHandler,
            onIntroSkip = mainViewModel::onIntroSkipHandler,
            onModeIntroSkip = mainViewModel::onModeIntroSkipHandler,
            onGuideCancel = mainViewModel::onCancelGuidePressedHandler,
            onEmergency = mainViewModel::onEmergencyHandler,
            onRideComplete = mainViewModel::onRideCompleteHandler,
            onNewRide = mainViewModel::onNewRideHandler,
            onBankPickerOpen = accountViewModel::onBankPickerOpenedHandler,
            onBankNameChange = accountViewModel::onBankSelectedHandler,
            onHolderNameChange = accountViewModel::onUpdateHolderNameHandler,
            onAccountNumberChange = accountViewModel::onUpdateAccountNumberHandler,
            onSettingsSave = {
                accountViewModel.onSaveHandler()
                mainViewModel.onSettingsSaveHandler()
            },
            onSettingsBack = {
                mainViewModel.onSettingsBackHandler()
                accountViewModel.onResetDraftHandler()
            },
            onPlayVideo = mainViewModel::onPlayVideoHandler,
            onAccountInfo = { mainViewModel.onShowAccountGuideHandler(bankAccount) },
            onAccountGuideRepeat = { mainViewModel.onAccountGuideRepeatHandler(bankAccount) },
            onAccountGuideConfirm = mainViewModel::onAccountGuideConfirmHandler,
            onAccountGuideSetup = mainViewModel::onAccountGuideSetupHandler,
            onStopRecordingFab = mainViewModel::onStopRecordingFabPressedHandler,
        )

        // ── Settings action sheet ─────────────────────────────────────────────

        if (viewState.showSettingsSheet) {
            ModalBottomSheet(
                onDismissRequest = mainViewModel::onSheetClosedHandler,
                sheetState = sheetState,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            ) {
                SettingsSheetContent(
                    darkModeEnabled = viewState.isDarkMode,
                    onAccountSettings = mainViewModel::onNavigateToAccountSettingsHandler,
                    onEmergencyVideos = mainViewModel::onNavigateToEmergencyVideosHandler,
                    onDarkModeToggle = mainViewModel::onDarkModeToggleHandler,
                )
            }
        }
    }
}

/** Dialogs. */
@Composable
private fun AppDialogs(
    showCancelGuide: Boolean,
    showExit: Boolean,
    showStopRecording: Boolean,
    onCancelGuideConfirm: () -> Unit,
    onCancelGuideDismiss: () -> Unit,
    onExitConfirm: () -> Unit,
    onExitDismiss: () -> Unit,
    onStopRecordingConfirm: () -> Unit,
    onStopRecordingDismiss: () -> Unit,
) {
    if (showCancelGuide) {
        AlertDialog(
            onDismissRequest = onCancelGuideDismiss,
            title = { Text(stringResource(R.string.cancel_guide_dialog_title)) },
            text = { Text(stringResource(R.string.cancel_guide_dialog_message)) },
            confirmButton = {
                TextButton(onClick = onCancelGuideConfirm) {
                    Text(stringResource(R.string.cancel_guide_dialog_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = onCancelGuideDismiss) {
                    Text(stringResource(R.string.cancel_guide_dialog_cancel))
                }
            },
        )
    }

    if (showExit) {
        AlertDialog(
            onDismissRequest = onExitDismiss,
            title = { Text(stringResource(R.string.exit_dialog_title)) },
            text = { Text(stringResource(R.string.exit_dialog_message)) },
            confirmButton = {
                TextButton(onClick = onExitConfirm) {
                    Text(stringResource(R.string.exit_dialog_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = onExitDismiss) {
                    Text(stringResource(R.string.exit_dialog_cancel))
                }
            },
        )
    }

    if (showStopRecording) {
        AlertDialog(
            onDismissRequest = onStopRecordingDismiss,
            title = { Text(stringResource(R.string.stop_recording_dialog_title)) },
            text = { Text(stringResource(R.string.stop_recording_dialog_message)) },
            confirmButton = {
                TextButton(onClick = onStopRecordingConfirm) {
                    Text(stringResource(R.string.stop_recording_dialog_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = onStopRecordingDismiss) {
                    Text(stringResource(R.string.stop_recording_dialog_cancel))
                }
            },
        )
    }
}

/** Screen content. */
@Suppress("LongParameterList")
@Composable
private fun AppScreenContent(
    viewState: MainContract.ViewState,
    draft: BankAccount,
    isDirty: Boolean,
    bankAccount: BankAccount,
    onBack: () -> Unit,
    onModeChange: (DriveMode) -> Unit,
    onOpenSettings: () -> Unit,
    onStartGuide: () -> Unit,
    onIntroSkip: () -> Unit,
    onModeIntroSkip: () -> Unit,
    onGuideCancel: () -> Unit,
    onEmergency: () -> Unit,
    onRideComplete: () -> Unit,
    onNewRide: () -> Unit,
    onBankPickerOpen: () -> Unit,
    onBankNameChange: (String) -> Unit,
    onHolderNameChange: (String) -> Unit,
    onAccountNumberChange: (String) -> Unit,
    onSettingsSave: () -> Unit,
    onSettingsBack: () -> Unit,
    onPlayVideo: (File) -> Unit,
    onAccountInfo: () -> Unit,
    onAccountGuideRepeat: () -> Unit,
    onAccountGuideConfirm: () -> Unit,
    onAccountGuideSetup: () -> Unit,
    onStopRecordingFab: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (viewState.currentScreen) {
            AppScreen.STANDBY -> MainScreen(
                selectedMode = viewState.selectedMode,
                isRiding = viewState.isRiding,
                isRecording = viewState.isRecording,
                onModeChange = onModeChange,
                onOpenSettings = onOpenSettings,
                onStartGuide = onStartGuide,
                onComplete = onRideComplete,
                onAccountInfo = onAccountInfo,
                onEmergency = onEmergency,
            )
            AppScreen.INTRO -> VoiceGuideScreen(
                selectedMode = viewState.selectedMode ?: DriveMode.QUIET,
                isPlaying = viewState.isPlayingAudio,
                onSkip = onIntroSkip,
                onCancel = onGuideCancel,
            )
            AppScreen.MODE_INTRO -> VoiceGuideScreen(
                selectedMode = viewState.selectedMode ?: DriveMode.QUIET,
                isPlaying = viewState.isPlayingAudio,
                onSkip = onModeIntroSkip,
                onCancel = onGuideCancel,
            )
            AppScreen.COMPLETED -> CompletedScreen(
                onNewRide = onNewRide,
            )
            AppScreen.SETTINGS -> AccountSettingsScreen(
                bankName = draft.bankName,
                holderName = draft.holderName,
                accountNumber = draft.accountNumber,
                isDirty = isDirty,
                onBankPickerOpen = onBankPickerOpen,
                onBankNameChange = onBankNameChange,
                onHolderNameChange = onHolderNameChange,
                onAccountNumberChange = onAccountNumberChange,
                onSave = onSettingsSave,
                onBack = onSettingsBack,
            )
            AppScreen.EMERGENCY_VIDEOS -> EmergencyVideoScreen(
                onBack = onBack,
                onPlayVideo = onPlayVideo,
            )
            AppScreen.ACCOUNT_GUIDE -> AccountGuideScreen(
                bankAccount = bankAccount,
                onRepeat = onAccountGuideRepeat,
                onConfirm = onAccountGuideConfirm,
                onSetupAccount = onAccountGuideSetup,
            )
        }

        if (viewState.isRecording) {
            RecordingStopFab(
                onClick = onStopRecordingFab,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .navigationBarsPadding()
                    .padding(end = 24.dp, bottom = 24.dp),
            )
        }
    }
}

/** Settings sheet content. */
@Composable
private fun SettingsSheetContent(
    darkModeEnabled: Boolean,
    onAccountSettings: () -> Unit,
    onEmergencyVideos: () -> Unit,
    onDarkModeToggle: (Boolean) -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 8.dp),
    ) {
        Text(
            text = stringResource(R.string.settings_sheet_title),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
            ),
            color = colorScheme.onSurface.copy(alpha = 0.5f),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
        )

        HorizontalDivider(color = colorScheme.onSurface.copy(alpha = 0.08f))

        SettingsSheetSwitchItem(
            icon = Icons.Filled.DarkMode,
            title = stringResource(R.string.settings_sheet_dark_mode),
            desc = stringResource(R.string.settings_sheet_dark_mode_desc),
            checked = darkModeEnabled,
            onCheckedChange = onDarkModeToggle,
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 24.dp),
            color = colorScheme.onSurface.copy(alpha = 0.06f),
        )

        SettingsSheetItem(
            icon = Icons.Filled.AccountBalance,
            title = stringResource(R.string.settings_sheet_account),
            desc = stringResource(R.string.settings_sheet_account_desc),
            onClick = onAccountSettings,
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 24.dp),
            color = colorScheme.onSurface.copy(alpha = 0.06f),
        )

        SettingsSheetItem(
            icon = Icons.Filled.VideoLibrary,
            title = stringResource(R.string.settings_sheet_emergency_videos),
            desc = stringResource(R.string.settings_sheet_emergency_videos_desc),
            onClick = onEmergencyVideos,
        )

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun SettingsSheetItem(
    icon: ImageVector,
    title: String,
    desc: String,
    onClick: () -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(colorScheme.primary.copy(alpha = 0.10f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(22.dp),
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                ),
                color = colorScheme.onSurface,
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.labelSmall,
                color = colorScheme.onSurface.copy(alpha = 0.5f),
            )
        }
    }
}

@Composable
private fun SettingsSheetSwitchItem(
    icon: ImageVector,
    title: String,
    desc: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(colorScheme.primary.copy(alpha = 0.10f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(22.dp),
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                ),
                color = colorScheme.onSurface,
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.labelSmall,
                color = colorScheme.onSurface.copy(alpha = 0.5f),
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

/** Recording stop FAB. */
@Composable
private fun RecordingStopFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition(label = "rec_ring")
    val ringAlpha by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "ring_alpha",
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(72.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFDC2626).copy(alpha = ringAlpha), CircleShape),
        )
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier.size(56.dp),
            shape = CircleShape,
            containerColor = Color(0xFFDC2626),
            contentColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Stop,
                contentDescription = stringResource(R.string.stop_recording_fab_desc),
                modifier = Modifier.size(26.dp),
            )
        }
    }
}
