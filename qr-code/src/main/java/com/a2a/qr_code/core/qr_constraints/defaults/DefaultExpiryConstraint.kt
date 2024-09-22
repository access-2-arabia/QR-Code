package com.a2a.qr_code.core.qr_constraints.defaults

import com.a2a.qr_code.core.qr_constraints.QrFiledConstraint
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * A default implementation of [QrFiledConstraint] for validating expiry fields in QR codes.
 *
 * This class checks whether the provided expiry value is not blank. It ensures that the value
 * is a non-null string and contains at least one non-whitespace character, indicating that
 * an expiry date has been provided.
 */
@Parcelize
class DefaultExpiryConstraint : QrFiledConstraint<String> {

    /**
     * Validates the provided expiry value.
     *
     * @param value The expiry value as a string to be validated. It can be null.
     * @return `true` if the value is not blank; `false` otherwise.
     */
    override fun validate(value: String?): Boolean {
        if (value.isNullOrBlank()) return false

        // Define the date format expected for the expiry date
        val formatter = DateTimeFormatter.ofPattern("M/d/yyyy")
        return try {
            val expiryDate = LocalDate.parse(value, formatter)
            !expiryDate.isBefore(LocalDate.now())
        } catch (e: DateTimeParseException) {
            false
        }
    }
}