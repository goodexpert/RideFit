package org.goodexpert.ridefit.ads

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.get
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Firebase Remote Config feature toggles for ads, exposed as an observable [StateFlow]
 * so Compose recomposes when values change at runtime (real-time Remote Config updates).
 *
 * Console keys (Boolean):
 *  - `ads_enabled`         — master kill-switch for all ads
 *  - `app_open_ad_enabled` — App Open ads
 *  - `banner_ad_enabled`   — banner ads
 *
 * Defaults are "on"; reads are guarded so ads keep working if Remote Config isn't
 * initialised (e.g. unit tests).
 */
object AdsFeatureConfig {

    private const val KEY_ADS_ENABLED = "ads_enabled"
    private const val KEY_APP_OPEN_ENABLED = "app_open_ad_enabled"
    private const val KEY_BANNER_ENABLED = "banner_ad_enabled"

    // Release: fetch at most hourly (Remote Config recommended production minimum).
    private const val RELEASE_FETCH_INTERVAL_SECONDS = 3600L

    data class Toggles(
        val appOpen: Boolean = true,
        val banner: Boolean = true,
    )

    private val remoteConfig by lazy { Firebase.remoteConfig }

    private val _toggles = MutableStateFlow(Toggles())

    /** Observe from Compose via `collectAsState()`; read synchronously via `.value`. */
    val toggles: StateFlow<Toggles> = _toggles.asStateFlow()

    // Convenience synchronous reads (used by non-Compose callers like the App Open flow).
    val appOpenAdEnabled: Boolean get() = _toggles.value.appOpen
    val bannerAdEnabled: Boolean get() = _toggles.value.banner

    /** Set defaults, fetch, and subscribe to real-time updates. Call from Application.onCreate. */
    fun init(debuggable: Boolean) {
        runCatching {
            remoteConfig.setConfigSettingsAsync(
                remoteConfigSettings {
                    // Debug: fetch every launch so toggles take effect immediately.
                    minimumFetchIntervalInSeconds =
                        if (debuggable) 0 else RELEASE_FETCH_INTERVAL_SECONDS
                },
            )
            remoteConfig.setDefaultsAsync(
                mapOf(
                    KEY_ADS_ENABLED to true,
                    KEY_APP_OPEN_ENABLED to true,
                    KEY_BANNER_ENABLED to true,
                ),
            )
            remoteConfig.fetchAndActivate().addOnCompleteListener { refresh() }

            // Real-time updates: push server changes without an app restart.
            remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
                override fun onUpdate(configUpdate: ConfigUpdate) {
                    remoteConfig.activate().addOnCompleteListener { refresh() }
                }

                override fun onError(error: FirebaseRemoteConfigException) {
                    Log.w(TAG, "Remote Config realtime error", error)
                }
            })
        }
        refresh()
    }

    /** Re-fetch + activate and update [toggles]. Call on app foreground for reliability. */
    fun refreshFromRemote() {
        runCatching {
            remoteConfig.fetchAndActivate().addOnCompleteListener { refresh() }
        }
    }

    private fun refresh() {
        val ads = boolean(KEY_ADS_ENABLED)
        _toggles.value = Toggles(
            appOpen = ads && boolean(KEY_APP_OPEN_ENABLED),
            banner = ads && boolean(KEY_BANNER_ENABLED),
        )
    }

    private const val TAG = "AdsConfig"

    private fun boolean(key: String): Boolean = runCatching {
        val value = remoteConfig[key]
        // Until Remote Config resolves a value (source STATIC = defaults/fetch not yet
        // applied), default to on so ads aren't suppressed by the async startup timing.
        if (value.source == FirebaseRemoteConfig.VALUE_SOURCE_STATIC) true else value.asBoolean()
    }.getOrDefault(false)
}
