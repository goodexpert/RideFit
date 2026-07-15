package org.goodexpert.ridefit

import android.app.Activity
import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import org.goodexpert.ridefit.ads.AppOpenAdManager

// AdMob test device hash for the developer's device — only applied in debuggable
// builds so release builds serve real (revenue-earning) ads.
private const val TEST_DEVICE_ID = "D8FD84DA4C3EAA567F8ED2022B27A965"

/**
 * Initialises the Google Mobile Ads SDK and shows an App Open ad whenever the app
 * enters the foreground — cold start (once an ad is cached) and every return from
 * background — before the current screen is shown.
 *
 * Uses [ProcessLifecycleOwner] to detect foreground transitions and
 * [Application.ActivityLifecycleCallbacks] to know which Activity to show the ad over.
 */
class AppApplication : Application(), Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {

    private lateinit var appOpenAdManager: AppOpenAdManager
    private var currentActivity: Activity? = null
    private var isForeground = false

    // On cold start the ad usually isn't loaded when the app first foregrounds, so we
    // remember to show it once it finishes loading (one-shot).
    private var pendingColdStartShow = false

    override fun onCreate() {
        super<Application>.onCreate()
        registerActivityLifecycleCallbacks(this)

        val isDebuggable = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        if (isDebuggable) {
            MobileAds.setRequestConfiguration(
                RequestConfiguration.Builder()
                    .setTestDeviceIds(listOf(TEST_DEVICE_ID))
                    .build(),
            )
        }

        MobileAds.initialize(this) {}
        appOpenAdManager = AppOpenAdManager()
        appOpenAdManager.onAdLoaded = {
            // Cold start: the ad wasn't ready at first foreground — show it now.
            if (pendingColdStartShow && isForeground) {
                pendingColdStartShow = false
                currentActivity?.let { appOpenAdManager.showAdIfAvailable(it) }
            }
        }
        appOpenAdManager.loadAd(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    /** Foreground entry (cold start after first Activity + every resume). */
    override fun onStart(owner: LifecycleOwner) {
        isForeground = true
        val activity = currentActivity ?: return
        // If no ad is ready yet (typical cold start), show it as soon as it loads.
        if (!appOpenAdManager.showAdIfAvailable(activity)) {
            pendingColdStartShow = true
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        isForeground = false
    }

    override fun onActivityStarted(activity: Activity) {
        // Don't overwrite while the ad Activity is in front.
        if (!appOpenAdManager.isShowingAd) currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        if (!appOpenAdManager.isShowingAd) currentActivity = activity
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}
