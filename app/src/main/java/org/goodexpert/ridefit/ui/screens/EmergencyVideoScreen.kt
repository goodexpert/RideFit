package org.goodexpert.ridefit.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.goodexpert.ridefit.R
import org.goodexpert.ridefit.ui.components.BannerAd
import org.goodexpert.ridefit.ui.theme.RideFitTheme
import org.goodexpert.ridefit.ui.theme.rideFitColors
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun EmergencyVideoScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onPlayVideo: (File) -> Unit = {},
    onShareVideo: (File) -> Unit = {},
) {
    val context = LocalContext.current
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
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding(),
    ) {
        VideoListHeader(onBack = onBack)

        if (videoFiles.isEmpty()) {
            EmptyState(modifier = Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
            ) {
                items(videoFiles, key = { it.absolutePath }) { file ->
                    SwipeableVideoItem(
                        file = file,
                        onClick = { onPlayVideo(file) },
                        onShare = { onShareVideo(file) },
                        onDelete = {
                            videoFiles.remove(file)
                            scope.launch(Dispatchers.IO) { file.delete() }
                        },
                    )
                }
            }
        }

        // 하단 고정 배너 광고
        BannerAd()
    }
}

@Composable
private fun VideoListHeader(onBack: () -> Unit) {
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
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
        Text(
            text = stringResource(R.string.emergency_videos_title),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Black,
                fontSize = 22.sp,
            ),
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    color = MaterialTheme.rideFitColors.emergencyContainer,
                    shape = RoundedCornerShape(20.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Videocam,
                contentDescription = null,
                tint = MaterialTheme.rideFitColors.emergencyRed.copy(alpha = 0.5f),
                modifier = Modifier.size(36.dp),
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.emergency_videos_empty),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.emergency_videos_empty_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * A video row that reveals fixed [공유 / 삭제] action buttons when swiped left. The card
 * rests open (anchored) so the buttons are tappable — it never deletes on a full swipe, so an
 * emergency clip can't be lost by an accidental fling. Tapping the open card (or swiping back
 * right) closes it; tapping the closed card plays the video.
 */
@Composable
private fun SwipeableVideoItem(
    file: File,
    onClick: () -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val actionsWidthPx = with(LocalDensity.current) { ACTIONS_WIDTH.toPx() }
    // Live drag offset updated synchronously (no per-delta coroutine → no race with settle).
    var offsetX by remember { mutableFloatStateOf(0f) }
    var settleJob by remember { mutableStateOf<Job?>(null) }

    fun settleTo(target: Float) {
        settleJob?.cancel()
        settleJob = scope.launch {
            animate(offsetX, target, animationSpec = tween(REVEAL_ANIM_MS)) { value, _ ->
                offsetX = value
            }
        }
    }

    val draggableState = rememberDraggableState { delta ->
        offsetX = (offsetX + delta).coerceIn(-actionsWidthPx, 0f)
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        // Revealed action buttons, pinned to the trailing edge behind the card.
        Row(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(8.dp)),
            horizontalArrangement = Arrangement.End,
        ) {
            SwipeActionButton(
                icon = Icons.Filled.Share,
                label = stringResource(R.string.emergency_video_share),
                background = MaterialTheme.rideFitColors.brand,
                onClick = {
                    settleTo(0f)
                    onShare()
                },
            )
            SwipeActionButton(
                icon = Icons.Filled.Delete,
                label = stringResource(R.string.emergency_video_delete),
                background = MaterialTheme.rideFitColors.emergencyRed,
                onClick = onDelete,
            )
        }

        // Foreground card, slid left to reveal the actions.
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .draggable(
                    state = draggableState,
                    orientation = Orientation.Horizontal,
                    onDragStarted = { settleJob?.cancel() },
                    onDragStopped = {
                        settleTo(if (offsetX <= -actionsWidthPx / 2f) -actionsWidthPx else 0f)
                    },
                ),
        ) {
            VideoItem(
                file = file,
                onClick = { if (offsetX != 0f) settleTo(0f) else onClick() },
            )
        }
    }
}

@Composable
private fun RowScope.SwipeActionButton(
    icon: ImageVector,
    label: String,
    background: Color,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(ACTION_BUTTON_WIDTH)
            .background(background)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(30.dp),
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = Color.White,
        )
    }
}

@Composable
private fun VideoItem(file: File, onClick: () -> Unit) {
    val bgColor     = MaterialTheme.rideFitColors.cardContainer
    val borderColor = MaterialTheme.rideFitColors.cardBorder

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
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
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = file.displaySize(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                )
            }
            Icon(
                imageVector = Icons.Filled.PlayCircle,
                contentDescription = stringResource(R.string.emergency_video_play_desc),
                tint = MaterialTheme.rideFitColors.brand,
                modifier = Modifier.size(32.dp),
            )
        }
    }
}

@Composable
private fun VideoThumbnail(file: File, modifier: Modifier = Modifier) {
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
            color = MaterialTheme.rideFitColors.subtleContainer,
            shape = RoundedCornerShape(8.dp),
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
                    .clip(RoundedCornerShape(4.dp)),
            )
        } else {
            Icon(
                imageVector = Icons.Filled.Videocam,
                contentDescription = null,
                tint = MaterialTheme.rideFitColors.brand.copy(alpha = 0.4f),
                modifier = Modifier.size(32.dp),
            )
        }
    }
}

private fun File.displayDateTime(): String =
    runCatching {
        val raw = name.removePrefix("emergency_").removeSuffix(".mp4")
        val date = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).parse(raw)!!
        SimpleDateFormat("yyyy년 MM월 dd일\nHH:mm:ss", Locale.KOREAN).format(date)
    }.getOrElse {
        SimpleDateFormat("yyyy년 MM월 dd일\nHH:mm:ss", Locale.KOREAN).format(Date(lastModified()))
    }

private val ACTION_BUTTON_WIDTH = 76.dp
private val ACTIONS_WIDTH = ACTION_BUTTON_WIDTH * 2
private const val REVEAL_ANIM_MS = 250
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

// Non-existent file: the timestamped name drives displayDateTime(); the thumbnail
// decode fails (no real mp4) so the placeholder state is what renders.
private val previewVideoFile = File("emergency_20260715_143025.mp4")

@Preview(name = "VideoItem · Day", showBackground = true, widthDp = 360)
@Composable
private fun VideoItemDayPreview() {
    RideFitTheme(darkTheme = false) {
        VideoItem(file = previewVideoFile, onClick = {})
    }
}

@Preview(name = "VideoItem · Night", showBackground = true, widthDp = 360, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun VideoItemNightPreview() {
    RideFitTheme(darkTheme = true) {
        VideoItem(file = previewVideoFile, onClick = {})
    }
}

@Preview(name = "VideoThumbnail · Day", showBackground = true)
@Composable
private fun VideoThumbnailDayPreview() {
    RideFitTheme(darkTheme = false) {
        VideoThumbnail(
            file = previewVideoFile,
            modifier = Modifier
                .width(108.dp)
                .height(72.dp),
        )
    }
}

@Preview(name = "VideoThumbnail · Night", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun VideoThumbnailNightPreview() {
    RideFitTheme(darkTheme = true) {
        VideoThumbnail(
            file = previewVideoFile,
            modifier = Modifier
                .width(108.dp)
                .height(72.dp),
        )
    }
}
