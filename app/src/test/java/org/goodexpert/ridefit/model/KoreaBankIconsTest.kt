package org.goodexpert.ridefit.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

@Suppress("TooManyFunctions")
class KoreaBankIconsTest {

    // ── isBank ────────────────────────────────────────────────────────────────

    @Test
    fun isBank_trueForBankCodes() {
        assertTrue(KoreaBankIcons.isBank("004"))
        assertTrue(KoreaBankIcons.isBank("090"))
        assertTrue(KoreaBankIcons.isBank("092"))
    }

    @Test
    fun isBank_falseForSecuritiesCodes() {
        assertFalse(KoreaBankIcons.isBank("238"))
        assertFalse(KoreaBankIcons.isBank("271"))
    }

    @Test
    fun isBank_falseForUnknownCode() {
        assertFalse(KoreaBankIcons.isBank("999"))
    }

    // ── isSecurities ─────────────────────────────────────────────────────────

    @Test
    fun isSecurities_trueForSecuritiesCodes() {
        assertTrue(KoreaBankIcons.isSecurities("238"))
        assertTrue(KoreaBankIcons.isSecurities("271"))
    }

    @Test
    fun isSecurities_falseForBankCodes() {
        assertFalse(KoreaBankIcons.isSecurities("004"))
        assertFalse(KoreaBankIcons.isSecurities("090"))
    }

    @Test
    fun isSecurities_falseForUnknownCode() {
        assertFalse(KoreaBankIcons.isSecurities("999"))
    }

    // ── hasIcon ───────────────────────────────────────────────────────────────

    @Test
    fun hasIcon_trueForKnownBankCode() {
        assertTrue(KoreaBankIcons.hasIcon("004"))
        assertTrue(KoreaBankIcons.hasIcon("090"))
    }

    @Test
    fun hasIcon_trueForKnownSecuritiesCode() {
        assertTrue(KoreaBankIcons.hasIcon("238"))
    }

    @Test
    fun hasIcon_falseForUnknownCode() {
        assertFalse(KoreaBankIcons.hasIcon("999"))
        assertFalse(KoreaBankIcons.hasIcon("000"))
    }

    // ── getName ───────────────────────────────────────────────────────────────

    @Test
    fun getName_returnsCorrectBankName() {
        assertEquals("KB국민은행", KoreaBankIcons.getName("004"))
        assertEquals("카카오뱅크", KoreaBankIcons.getName("090"))
        assertEquals("토스뱅크", KoreaBankIcons.getName("092"))
    }

    @Test
    fun getName_returnsCorrectSecuritiesName() {
        assertEquals("미래에셋증권", KoreaBankIcons.getName("238"))
        assertEquals("토스증권", KoreaBankIcons.getName("271"))
    }

    @Test
    fun getName_returnsNullForUnknownCode() {
        assertNull(KoreaBankIcons.getName("999"))
    }

    // ── code normalisation ────────────────────────────────────────────────────

    @Test
    fun getName_normalizesShortCode() {
        assertEquals("KB국민은행", KoreaBankIcons.getName("4"))
        assertEquals("카카오뱅크", KoreaBankIcons.getName("90"))
    }

    @Test
    fun hasIcon_normalizesShortCode() {
        assertTrue(KoreaBankIcons.hasIcon("4"))
        assertTrue(KoreaBankIcons.hasIcon("90"))
    }

    // ── data completeness ─────────────────────────────────────────────────────

    @Test
    fun banks_hasExpectedCount() {
        assertEquals(29, KoreaBankIcons.banks.size)
    }

    @Test
    fun securities_hasExpectedCount() {
        assertEquals(25, KoreaBankIcons.securities.size)
    }

    @Test
    fun all_containsBanksAndSecurities() {
        assertEquals(
            KoreaBankIcons.banks.size + KoreaBankIcons.securities.size,
            KoreaBankIcons.all.size,
        )
    }
}
