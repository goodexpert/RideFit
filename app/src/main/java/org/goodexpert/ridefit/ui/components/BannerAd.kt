package org.goodexpert.ridefit.ui.components

import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

// Google sample banner unit — always fills a test ad (works before account approval).
private const val SAMPLE_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"

// RideFit real Banner ad unit — used in release / non-debuggable builds.
private const val REAL_BANNER_AD_UNIT_ID = "ca-app-pub-3542184687162386/5070588723"

/**
 * An adaptive-width AdMob banner. In debuggable builds it uses Google's sample banner
 * unit (so it renders test ads even before the AdMob account is approved); release
 * builds use the real unit. Renders an empty placeholder in Compose previews.
 */
@Composable
fun BannerAd(modifier: Modifier = Modifier) {
    // Don't instantiate a real AdView in @Preview / inspection tooling.
    if (LocalInspectionMode.current) {
        Box(modifier.fillMaxWidth().height(50.dp))
        return
    }

    val context = LocalContext.current
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { ctx ->
            AdView(ctx).apply {
                setAdSize(adaptiveAdSize(ctx))
                adUnitId = bannerAdUnitId(ctx)
                loadAd(AdRequest.Builder().build())
            }
        },
        onRelease = { it.destroy() },
    )
}

private fun adaptiveAdSize(context: Context): AdSize {
    val metrics = context.resources.displayMetrics
    val adWidthDp = (metrics.widthPixels / metrics.density).toInt()
    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidthDp)
}

private fun bannerAdUnitId(context: Context): String {
    val debuggable = (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    return if (debuggable) SAMPLE_BANNER_AD_UNIT_ID else REAL_BANNER_AD_UNIT_ID
}
