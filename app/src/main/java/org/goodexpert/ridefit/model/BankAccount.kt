package org.goodexpert.ridefit.model

data class BankAccount(
    val bankName: String = "",
    val holderName: String = "",
    val accountNumber: String = "",
) {
    val isConfigured: Boolean
        get() = bankName.isNotBlank() && holderName.isNotBlank() && accountNumber.isNotBlank()

    val spokenAccountNumber: String
        get() = accountNumber
            .split("-")
            .joinToString(", ") { group -> group.map { it.toKoreanDigit() }.joinToString(" ") }

    val digits: List<String>
        get() = accountNumber.filter { it.isDigit() }.map { it.toKoreanDigit() }
}

private fun Char.toKoreanDigit(): String = when (this) {
    '0' -> "공"
    '1' -> "일"
    '2' -> "이"
    '3' -> "삼"
    '4' -> "사"
    '5' -> "오"
    '6' -> "육"
    '7' -> "칠"
    '8' -> "팔"
    '9' -> "구"
    else -> toString()
}
