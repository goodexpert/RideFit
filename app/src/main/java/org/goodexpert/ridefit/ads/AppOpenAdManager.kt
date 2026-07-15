package org.goodexpert.ridefit.ads

import android.app.Activity
import android.content.Context
import android.content.pm.ApplicationInfo
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import java.util.Date

private const val TAG = "AppOpenAd"

// RideFit real App Open ad unit — used in release / non-debuggable builds.
// (Do NOT click real ads on your own device — that risks account suspension.)
private const val REAL_AD_UNIT_ID = "ca-app-pub-3542184687162386/9992697206"

// Google sample App Open unit — always fills with a test ad, even before the AdMob
// account is approved. Used in debuggable builds to verify the ad flow.
private const val SAMPLE_AD_UNIT_ID = "ca-app-pub-3940256099942544/9257395921"

private fun adUnitId(context: Context): String {
    val debuggable = (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    return if (debuggable) SAMPLE_AD_UNIT_ID else REAL_AD_UNIT_ID
}

// App Open ads expire after 4 hours.
private const val AD_TIMEOUT_MS = 4L * 60 * 60 * 1000

/**
 * Loads and shows a single App Open ad, guarding against duplicate loads/shows and
 * honouring the 4-hour freshness window. Managed by [org.goodexpert.ridefit.AppApplication].
 */
class AppOpenAdManager {

    private var appOpenAd: AppOpenAd? = null
    private var isLoadingAd = false
    private var loadTime = 0L

    var isShowingAd = false
        private set

    /** Invoked when an ad finishes loading (used to show it on cold start). */
    var onAdLoaded: (() -> Unit)? = null

    /** Preload an ad if one isn't already loaded/loading. */
    fun loadAd(context: Context) {
        if (isLoadingAd || isAdAvailable()) return
        isLoadingAd = true

        AppOpenAd.load(
            context,
            adUnitId(context),
            AdRequest.Builder().build(),
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    isLoadingAd = false
                    loadTime = Date().time
                    onAdLoaded?.invoke()
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    isLoadingAd = false
                    Log.d(TAG, "onAdFailedToLoad: ${error.message}")
                }
            },
        )
    }

    /**
     * Shows the ad over [activity] if one is loaded and fresh; otherwise triggers a
     * load for next time. [onDismissed] runs once the ad is closed (or immediately if
     * there is no ad to show), so callers can continue into the app.
     *
     * @return true if an ad was shown, false if none was available.
     */
    fun showAdIfAvailable(activity: Activity, onDismissed: () -> Unit = {}): Boolean {
        if (isShowingAd) return false

        if (!isAdAvailable()) {
            onDismissed()
            loadAd(activity.applicationContext)
            return false
        }

        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                appOpenAd = null
                isShowingAd = false
                onDismissed()
                loadAd(activity.applicationContext)
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                appOpenAd = null
                isShowingAd = false
                onDismissed()
                loadAd(activity.applicationContext)
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "onAdShowedFullScreenContent")
            }
        }

        isShowingAd = true
        appOpenAd?.show(activity)
        return true
    }

    private fun isAdAvailable(): Boolean =
        appOpenAd != null && Date().time - loadTime < AD_TIMEOUT_MS
}
