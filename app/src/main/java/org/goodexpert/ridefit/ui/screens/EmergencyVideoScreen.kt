package org.goodexpert.ridefit.ui.screens

import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.goodexpert.ridefit.R
import org.goodexpert.ridefit.ui.theme.rideFitColors
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyVideoScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onPlayVideo: (File) -> Unit = {},
) {
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme
    val scope = rememberCoroutineScope()

    val videoFiles = remember { mutableStateListOf<File>() }

    LaunchedEffect(Unit) {
        val files = withContext(Dispatchers.IO) {
            val dir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES) ?: context.filesDir
            dir.listFiles { f -> f.name.startsWith("emergency_") && f.name.endsWith(".mp4") }
                ?.sortedByDescending { it.lastModified() }
                ?: emptyList()
        }
        videoFiles.clear()
        videoFiles.addAll(files)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .safeDrawingPadding(),
    ) {
        VideoListHeader(onBack = onBack)

        if (videoFiles.isEmpty()) {
            EmptyState(modifier = Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
            ) {
                items(videoFiles, key = { it.absolutePath }) { file ->
                    SwipeableVideoItem(
                        file = file,
                        onClick = { onPlayVideo(file) },
                    ) {
                        videoFiles.remove(file)
                        scope.launch(Dispatchers.IO) { file.delete() }
                    }
                }
            }
        }
    }
}

// ── Header ────────────────────────────────────────────────────────────────────

@Composable
private fun VideoListHeader(onBack: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = colorScheme.onBackground,
            )
        }
        Text(
            text = stringResource(R.string.emergency_videos_title),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Black,
                fontSize = 22.sp,
            ),
            color = colorScheme.onBackground,
        )
    }
}

// ── Empty state ───────────────────────────────────────────────────────────────

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    val appColors = MaterialTheme.rideFitColors

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    color = if (!appColors.isDark) Color(0xFFFFF5F5) else Color(0xFF1A0808),
                    shape = RoundedCornerShape(20.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Videocam,
                contentDescription = null,
                tint = appColors.emergencyRed.copy(alpha = 0.5f),
                modifier = Modifier.size(36.dp),
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.emergency_videos_empty),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.emergency_videos_empty_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = colorScheme.onBackground.copy(alpha = 0.5f),
            textAlign = TextAlign.Center,
        )
    }
}

// ── Swipeable video item ──────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeableVideoItem(
    file: File,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    var itemWidthPx by remember { mutableStateOf(0f) }
    // Ref pattern to avoid circular dependency: dismissState references itself in confirmValueChange.
    // Setting stateRef synchronously in composition guarantees it's populated before any gesture fires.
    val stateRef = remember { mutableStateOf<SwipeToDismissBoxState?>(null) }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                val state = stateRef.value ?: return@rememberSwipeToDismissBoxState false
                itemWidthPx > 0f && abs(state.requireOffset()) / itemWidthPx >= SWIPE_THRESHOLD
            } else {
                true
            }
        },
        positionalThreshold = { totalDistance -> totalDistance * SWIPE_THRESHOLD },
    )
    stateRef.value = dismissState

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        onDismiss = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
            }
        },
        backgroundContent = {
            DeleteBackground(
                isActive = dismissState.targetValue == SwipeToDismissBoxValue.EndToStart,
            )
        },
        modifier = Modifier.onSizeChanged { size -> itemWidthPx = size.width.toFloat() },
    ) {
        VideoItem(file = file, onClick = onClick)
    }
}

// ── Delete background ─────────────────────────────────────────────────────────

@Composable
private fun DeleteBackground(isActive: Boolean) {
    val bgColor by animateColorAsState(
        targetValue = if (isActive) Color(0xFFB91C1C) else Color(0xFFDC2626),
        label = "delete_bg",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Column(
            modifier = Modifier.padding(end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(26.dp),
            )
            Text(
                text = stringResource(R.string.emergency_video_delete),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
            )
        }
    }
}

// ── Video list item ───────────────────────────────────────────────────────────

@Composable
private fun VideoItem(file: File, onClick: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    val appColors = MaterialTheme.rideFitColors
    val isDark = appColors.isDark

    val bgColor     = if (!isDark) colorScheme.surface else Color(0xFF14142A)
    val borderColor = if (!isDark) Color(0xFFD0DEFF) else Color(0xFF2A2A4A)

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            VideoThumbnail(
                file = file,
                modifier = Modifier
                    .width(108.dp)
                    .height(72.dp),
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = file.displayDateTime(),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                    ),
                    color = colorScheme.onSurface,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = file.displaySize(),
                    style = MaterialTheme.typography.labelSmall,
                    color = colorScheme.onSurface.copy(alpha = 0.5f),
                )
            }
            Icon(
                imageVector = Icons.Filled.PlayCircle,
                contentDescription = stringResource(R.string.emergency_video_play_desc),
                tint = appColors.brand,
                modifier = Modifier.size(32.dp),
            )
        }
    }
}

// ── Thumbnail ─────────────────────────────────────────────────────────────────

@Composable
private fun VideoThumbnail(file: File, modifier: Modifier = Modifier) {
    val appColors = MaterialTheme.rideFitColors

    val thumbnail by produceState<Bitmap?>(initialValue = null, key1 = file.absolutePath) {
        value = withContext(Dispatchers.IO) {
            runCatching {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ThumbnailUtils.createVideoThumbnail(file, android.util.Size(320, 180), null)
                } else {
                    @Suppress("DEPRECATION")
                    ThumbnailUtils.createVideoThumbnail(
                        file.absolutePath,
                        MediaStore.Images.Thumbnails.MINI_KIND,
                    )
                }
            }.getOrNull()
        }
    }

    Box(
        modifier = modifier.background(
            color = if (!appColors.isDark) Color(0xFFE8EDFF) else Color(0xFF0A0A20),
            shape = RoundedCornerShape(10.dp),
        ),
        contentAlignment = Alignment.Center,
    ) {
        val bmp = thumbnail
        if (bmp != null) {
            Image(
                bitmap = bmp.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp)),
            )
        } else {
            Icon(
                imageVector = Icons.Filled.Videocam,
                contentDescription = null,
                tint = appColors.brand.copy(alpha = 0.4f),
                modifier = Modifier.size(28.dp),
            )
        }
    }
}

// ── File helpers ──────────────────────────────────────────────────────────────

private fun File.displayDateTime(): String =
    runCatching {
        val raw = name.removePrefix("emergency_").removeSuffix(".mp4")
        val date = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).parse(raw)!!
        SimpleDateFormat("yyyy년 MM월 dd일\nHH:mm:ss", Locale.KOREAN).format(date)
    }.getOrElse {
        SimpleDateFormat("yyyy년 MM월 dd일\nHH:mm:ss", Locale.KOREAN).format(Date(lastModified()))
    }

private const val SWIPE_THRESHOLD = 0.8f
private const val BYTES_PER_KB = 1024L
private const val BYTES_PER_MB = 1024L * 1024L

private fun File.displaySize(): String {
    val bytes = length()
    return when {
        bytes >= BYTES_PER_MB -> "%.1f MB".format(bytes / BYTES_PER_MB.toDouble())
        bytes >= BYTES_PER_KB -> "%.1f KB".format(bytes / BYTES_PER_KB.toDouble())
        else                  -> "$bytes B"
    }
}
