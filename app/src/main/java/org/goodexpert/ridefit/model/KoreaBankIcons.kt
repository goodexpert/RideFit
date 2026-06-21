package org.goodexpert.ridefit.model

import android.content.Context
import androidx.annotation.DrawableRes

/**
 * Utility for Korean Financial Institution Icons.
 * * Provides financial institution names and maps KFTC institution codes to Android drawable resource IDs.
 * * Source: Translated and ported from korea_bank_icons.dart
 */
object KoreaBankIcons {

    private const val DEFAULT_FALLBACK_RES_NAME = "ic_account_balance"

    val banks = mapOf(
        "002" to "KDB산업은행", "003" to "IBK기업은행", "004" to "KB국민은행",
        "007" to "수협은행", "011" to "NH농협은행", "020" to "우리은행",
        "023" to "SC제일은행", "027" to "씨티은행", "031" to "대구은행",
        "032" to "부산은행", "034" to "광주은행", "035" to "제주은행",
        "037" to "전북은행", "039" to "경남은행", "045" to "새마을금고",
        "048" to "신협", "050" to "저축은행", "054" to "HSBC",
        "055" to "도이치은행", "060" to "BOA", "062" to "중국공상은행",
        "063" to "중국건설은행", "064" to "산림조합", "071" to "우체국",
        "081" to "하나은행", "088" to "신한은행", "089" to "케이뱅크",
        "090" to "카카오뱅크", "092" to "토스뱅크",
    )

    val securities = mapOf(
        "209" to "유안타증권", "218" to "KB증권", "238" to "미래에셋증권",
        "240" to "삼성증권", "243" to "한국투자증권", "247" to "NH투자증권",
        "261" to "교보증권", "262" to "하이투자증권", "263" to "현대차증권",
        "264" to "키움증권", "265" to "이베스트투자증권", "266" to "SK증권",
        "267" to "대신증권", "269" to "한화투자증권", "270" to "하나금융투자",
        "271" to "토스증권", "278" to "신한금융투자", "279" to "DB금융투자",
        "280" to "유진투자증권", "287" to "메리츠증권", "288" to "카카오페이증권",
        "290" to "부국증권", "291" to "신영증권", "292" to "케이프투자증권",
        "294" to "한국포스증권",
    )

    val all: Map<String, String> get() = banks + securities

    val supportedCodes: List<String> get() = all.keys.toList()

    @DrawableRes
    fun getIconResId(context: Context, code: String): Int {
        val normalizedCode = normalizeCode(code)

        if (!hasIcon(normalizedCode)) {
            return getFallbackResId(context)
        }

        val resName = "ic_bank_$normalizedCode"
        val resId = context.resources.getIdentifier(resName, "drawable", context.packageName)

        return if (resId != 0) resId else getFallbackResId(context)
    }

    fun getName(code: String): String? {
        return all[normalizeCode(code)]
    }

    @Suppress("MagicNumber")
    fun isBank(code: String): Boolean {
        val codeNum = normalizeCode(code).toIntOrNull() ?: 0
        return codeNum in 1..99
    }

    @Suppress("MagicNumber")
    fun isSecurities(code: String): Boolean {
        val codeNum = normalizeCode(code).toIntOrNull() ?: 0
        return codeNum in 200..299
    }

    fun hasIcon(code: String): Boolean {
        return all.containsKey(normalizeCode(code))
    }

    @Suppress("MagicNumber")
    private fun normalizeCode(code: String): String {
        val digits = code.replace(Regex("[^0-9]"), "")
        return digits.padStart(3, '0')
    }

    @DrawableRes
    private fun getFallbackResId(context: Context): Int {
        val id = context.resources.getIdentifier(DEFAULT_FALLBACK_RES_NAME, "drawable", context.packageName)
        return if (id != 0) id else android.R.drawable.ic_menu_gallery
    }
}
