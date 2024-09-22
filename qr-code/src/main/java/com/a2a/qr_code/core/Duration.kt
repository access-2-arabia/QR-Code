package com.a2a.qr_code.core

import java.time.LocalDate
import java.time.format.DateTimeFormatter


class Duration(val days: Int = 0, val months: Int = 0) {

    private fun calculateExpiryDate(startDate: LocalDate): LocalDate {
        return startDate.plusDays(days.toLong()).plusMonths(months.toLong())

    }

    fun formatExpiryDate(): String {
        val expiryDate = calculateExpiryDate(LocalDate.now())
        val formatter = DateTimeFormatter.ofPattern("M/d/yyyy")
        return expiryDate.format(formatter)
    }
}