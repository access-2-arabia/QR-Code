package com.a2a.qr_code.core.qr_constraints.defaults

import com.a2a.qr_code.core.qr_constraints.QrFiledConstraint
import kotlinx.parcelize.Parcelize

/**
 * A default implementation of [QrFiledConstraint] for validating identifier fields in QR codes.
 *
 * This class checks whether the provided identifier value is not blank. It ensures that the value
 * is a non-null string and contains at least one non-whitespace character, indicating that
 * a valid identifier has been provided.
 */
@Parcelize
class DefaultIdentifierConstraint : QrFiledConstraint<String> {

    /**
     * Validates the provided identifier value.
     *
     * @param value The identifier value as a string to be validated. It can be null.
     * @return `true` if the value is not blank; `false` otherwise.
     */
    override fun validate(value: String?): Boolean {
        return value?.isNotBlank() == true
    }

}