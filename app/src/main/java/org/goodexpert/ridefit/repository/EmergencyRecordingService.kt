package org.goodexpert.ridefit.repository

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EmergencyRecordingService : Service() {

    private var cameraDevice: CameraDevice? = null
    private var captureSession: CameraCaptureSession? = null
    private var mediaRecorder: MediaRecorder? = null
    private var recordingStarted = false
    private var outputFile: File? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                startForeground(RECORDING_NOTIFICATION_ID, buildRecordingNotification())
                startRecording()
            }
            ACTION_STOP -> stopRecordingAndSelf()
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        releaseResources()
        super.onDestroy()
    }

    @SuppressLint("MissingPermission")
    private fun startRecording() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            stopSelf()
            return
        }

        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val (frontCameraId, frontCharacteristics) = cameraManager.cameraIdList
            .mapNotNull { id ->
                val c = cameraManager.getCameraCharacteristics(id)
                if (c.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                    id to c
                } else {
                    null
                }
            }
            .firstOrNull() ?: run {
            stopSelf()
            return
        }

        val file = createOutputFile()
        outputFile = file

        val orientationHint = portraitOrientationHint(frontCharacteristics)

        @Suppress("DEPRECATION")
        val recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(this)
        } else {
            MediaRecorder()
        }

        runCatching {
            recorder.apply {
                setVideoSource(MediaRecorder.VideoSource.SURFACE)
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setVideoEncoder(MediaRecorder.VideoEncoder.H264)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setVideoSize(VIDEO_WIDTH, VIDEO_HEIGHT)
                setVideoFrameRate(VIDEO_FRAME_RATE)
                setVideoEncodingBitRate(VIDEO_ENCODING_BITRATE)
                setOrientationHint(orientationHint)
                setOutputFile(file.absolutePath)
                prepare()
            }
            mediaRecorder = recorder
        }.onFailure {
            recorder.release()
            stopSelf()
            return
        }

        val recorderSurface = recorder.surface
        openCameraAndRecord(cameraManager, frontCameraId, recorderSurface)
    }

    @SuppressLint("MissingPermission")
    private fun openCameraAndRecord(
        cameraManager: CameraManager,
        frontCameraId: String,
        recorderSurface: android.view.Surface,
    ) {
        runCatching {
            cameraManager.openCamera(
                frontCameraId,
                object : CameraDevice.StateCallback() {
                    override fun onOpened(camera: CameraDevice) {
                        cameraDevice = camera
                        runCatching {
                            @Suppress("DEPRECATION")
                            camera.createCaptureSession(
                                listOf(recorderSurface),
                                object : CameraCaptureSession.StateCallback() {
                                    override fun onConfigured(session: CameraCaptureSession) {
                                        captureSession = session
                                        runCatching {
                                            val request = camera
                                                .createCaptureRequest(CameraDevice.TEMPLATE_RECORD)
                                                .apply { addTarget(recorderSurface) }
                                                .build()
                                            session.setRepeatingRequest(request, null, null)
                                            mediaRecorder?.start()
                                            recordingStarted = true
                                        }.onFailure { stopRecordingAndSelf() }
                                    }

                                    override fun onConfigureFailed(session: CameraCaptureSession) {
                                        stopRecordingAndSelf()
                                    }
                                },
                                null,
                            )
                        }.onFailure { stopRecordingAndSelf() }
                    }

                    override fun onDisconnected(camera: CameraDevice) {
                        camera.close()
                        cameraDevice = null
                    }

                    override fun onError(camera: CameraDevice, error: Int) {
                        camera.close()
                        cameraDevice = null
                        stopSelf()
                    }
                },
                null,
            )
        }.onFailure { stopSelf() }
    }

    private fun stopRecordingAndSelf() {
        val savedFile = outputFile.takeIf { recordingStarted }
        releaseResources()
        @Suppress("DEPRECATION")
        stopForeground(true)
        if (savedFile != null && savedFile.exists() && savedFile.length() > 0) {
            sendSavedNotification(savedFile)
        }
        stopSelf()
    }

    private fun releaseResources() {
        runCatching {
            captureSession?.stopRepeating()
            captureSession?.close()
        }
        captureSession = null
        runCatching {
            if (recordingStarted) mediaRecorder?.stop()
            mediaRecorder?.reset()
            mediaRecorder?.release()
        }
        mediaRecorder = null
        recordingStarted = false
        runCatching { cameraDevice?.close() }
        cameraDevice = null
    }

    private fun sendSavedNotification(file: File) {
        val uri = runCatching {
            FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
        }.getOrNull() ?: return

        val openIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "video/mp4")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            SAVED_NOTIFICATION_ID,
            openIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        val savedAt = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss", Locale.KOREAN).format(Date())

        val notification = NotificationCompat.Builder(this, SAVED_CHANNEL_ID)
            .setContentTitle("긴급 영상 저장 완료")
            .setContentText("$savedAt · 탭하여 영상 확인")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        getSystemService(NotificationManager::class.java).notify(SAVED_NOTIFICATION_ID, notification)
    }

    // Phone is always portrait-mounted in a car holder, so display rotation is always 0°.
    // orientationHint = sensorOrientation tells the player how many degrees to rotate the
    // landscape-native sensor output so it appears upright (portrait) on screen.
    private fun portraitOrientationHint(characteristics: CameraCharacteristics): Int =
        characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION) ?: DEFAULT_SENSOR_ORIENTATION

    private fun createOutputFile(): File {
        val dir = getExternalFilesDir(Environment.DIRECTORY_MOVIES) ?: filesDir
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return File(dir, "emergency_$timestamp.mp4")
    }

    private fun createNotificationChannels() {
        val manager = getSystemService(NotificationManager::class.java)

        manager.createNotificationChannel(
            NotificationChannel(CHANNEL_ID, "긴급 녹화", NotificationManager.IMPORTANCE_LOW).apply {
                setSound(null, null)
                enableVibration(false)
            },
        )

        manager.createNotificationChannel(
            NotificationChannel(SAVED_CHANNEL_ID, "긴급 영상 저장 알림", NotificationManager.IMPORTANCE_HIGH),
        )
    }

    private fun buildRecordingNotification(): Notification =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("RideFit 긴급 녹화 중")
            .setContentText("영상과 음성이 저장되고 있습니다")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSilent(true)
            .setOngoing(true)
            .build()

    companion object {
        const val ACTION_START = "org.goodexpert.ridefit.EMERGENCY_RECORD_START"
        const val ACTION_STOP  = "org.goodexpert.ridefit.EMERGENCY_RECORD_STOP"
        private const val CHANNEL_ID              = "emergency_recording"
        private const val SAVED_CHANNEL_ID        = "emergency_saved"
        private const val RECORDING_NOTIFICATION_ID = 9001
        private const val SAVED_NOTIFICATION_ID     = 9002
        private const val VIDEO_WIDTH               = 640
        private const val VIDEO_HEIGHT              = 480
        private const val VIDEO_FRAME_RATE          = 15
        private const val VIDEO_ENCODING_BITRATE    = 1_000_000
        private const val DEFAULT_SENSOR_ORIENTATION = 270

        fun start(context: Context) {
            context.startForegroundService(
                Intent(context, EmergencyRecordingService::class.java).setAction(ACTION_START),
            )
        }

        fun stop(context: Context) {
            context.startService(
                Intent(context, EmergencyRecordingService::class.java).setAction(ACTION_STOP),
            )
        }
    }
}
