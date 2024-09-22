package com.a2a.qr_code.core.qr_constraints.defaults

import com.a2a.qr_code.core.qr_constraints.QrFiledConstraint
import kotlinx.parcelize.Parcelize

/**
 * A default implementation of [QrFiledConstraint] for validating amount fields in QR codes.
 *
 * This class checks whether the provided amount value is a valid number. It converts the string
 * representation of the amount to a double and returns true if the conversion is successful,
 * indicating that the amount is valid.
 */
@Parcelize
class DefaultAmountFieldConstraint : QrFiledConstraint<String> {

    /**
     * Validates the provided amount value.
     *
     * @param value The amount value as a string to be validated. It can be null.
     * @return `true` if the value is a valid number; `false` otherwise.
     */
    override fun validate(value: String?): Boolean {
        return value?.toDoubleOrNull() != null
    }
}