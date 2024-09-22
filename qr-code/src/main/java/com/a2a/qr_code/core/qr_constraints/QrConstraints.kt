package com.a2a.qr_code.core.qr_constraints

import android.os.Parcelable
import com.a2a.qr_code.core.qr_constraints.defaults.DefaultAmountFieldConstraint
import com.a2a.qr_code.core.qr_constraints.defaults.DefaultExpiryConstraint
import com.a2a.qr_code.core.qr_constraints.defaults.DefaultIdentifierConstraint
import kotlinx.parcelize.Parcelize

/**
 * Represents constraints for QR code fields, such as identifier, amount, and expiry.
 *
 * This class is used to enforce specific validation rules on the QR code data fields.
 *
 * @property identifierConstraint The constraint applied to the identifier field.
 * @property amountConstraint The constraint applied to the amount field.
 * @property expiryConstraint The constraint applied to the expiry field.
 */
@Parcelize
class QrConstraints private constructor(
    val identifierConstraint: QrFiledConstraint<String>,
    val amountConstraint: QrFiledConstraint<String>,
    val expiryConstraint: QrFiledConstraint<String>
) : Parcelable {

    /**
     * Builder class for constructing an instance of [QrConstraints].
     *
     * This class provides a convenient way to set constraints for each field before building
     * the final [QrConstraints] object.
     */
    class Builder {
        private var identifierConstraint: QrFiledConstraint<String> = DefaultIdentifierConstraint()
        private var amountConstraint: QrFiledConstraint<String> = DefaultAmountFieldConstraint()
        private var expiryConstraint: QrFiledConstraint<String> = DefaultExpiryConstraint()

        /**
         * Sets the constraint for the identifier field.
         *
         * @param identifierConstraint The [QrFiledConstraint] to apply to the identifier.
         * @return The Builder instance for method chaining.
         */
        fun setIdentifierConstraint(identifierConstraint: QrFiledConstraint<String>) = apply {
            this.identifierConstraint = identifierConstraint
        }

        /**
         * Sets the constraint for the amount field.
         *
         * @param amountConstraint The [QrFiledConstraint] to apply to the amount.
         * @return The Builder instance for method chaining.
         */
        fun setAmountConstraint(amountConstraint: QrFiledConstraint<String>) = apply {
            this.amountConstraint = amountConstraint
        }

        /**
         * Sets the constraint for the expiry field.
         *
         * @param expiryConstraint The [QrFiledConstraint] to apply to the expiry.
         * @return The Builder instance for method chaining.
         */
        fun setExpiryConstraint(expiryConstraint: QrFiledConstraint<String>) = apply {
            this.expiryConstraint = expiryConstraint
        }

        /**
         * Builds an instance of [QrConstraints] with the specified field constraints.
         *
         * @return A new instance of [QrConstraints] with the configured constraints.
         */
        fun build(): QrConstraints {
            return QrConstraints(
                identifierConstraint,
                amountConstraint,
                expiryConstraint
            )
        }
    }
}
