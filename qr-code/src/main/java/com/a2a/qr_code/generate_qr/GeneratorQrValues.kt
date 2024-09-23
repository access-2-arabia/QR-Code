package com.a2a.qr_code.generate_qr

import com.a2a.qr_code.core.AMOUNT
import com.a2a.qr_code.core.Duration
import com.a2a.qr_code.core.EXPIRY
import com.a2a.qr_code.core.IDENTIFIER
import com.a2a.qr_code.core.formatQrUri
import com.a2a.qr_code.core.qr_constraints.QrConstraints
import com.a2a.qr_code.exceptions.InvalidQrFiled

/**
 * Data class for holding values to be encoded in a QR code.
 *
 * This class encapsulates the identifier, amount, and expiry values that are necessary
 * for generating a QR code. It provides a Builder pattern for constructing instances
 * while ensuring that the values meet specified constraints.
 *
 * @property identifier The unique identifier for the QR code.
 * @property amount The amount associated with the QR code.
 * @property expiry The expiry date of the QR code in a specified format.
 */
class GeneratorQrValues private constructor(
    private val identifier: String,
    private val amount: String,
    private val expiry: Duration
) {

    /**
     * Builder class for constructing instances of [GeneratorQrValues].
     *
     * This builder allows for setting each value individually and validates the values
     * against specified constraints before building the final object.
     */
    class Builder {

        private var identifier: String = ""
        private var amount: String = ""
        private var expiry: Duration = Duration(days = 2)
        private var qrConstraints: QrConstraints = QrConstraints.Builder().build()

        /**
         * Sets the identifier for the QR code.
         *
         * @param identifier The unique identifier.
         * @return The Builder instance for method chaining.
         */
        fun setIdentifier(identifier: String) = apply {
            this.identifier = identifier
        }

        /**
         * Sets the amount for the QR code.
         *
         * @param amount The amount associated with the QR code.
         * @return The Builder instance for method chaining.
         */
        fun setAmount(amount: String) = apply {
            this.amount = amount
        }

        /**
         * Sets the expiry date for the QR code.
         *
         * @param expiry The expiry date in a specified format.
         * @return The Builder instance for method chaining.
         */
        fun setExpiry(expiry: Duration) = apply {
            this.expiry = expiry
        }


        /**
         * Sets the constraints for validating the QR code fields.
         *
         * @param qrConstraints The constraints to be applied to the QR code fields.
         * @return The Builder instance for method chaining.
         */
        fun setQrConstraints(qrConstraints: QrConstraints) = apply {
            this.qrConstraints = qrConstraints
        }


        /**
         * Builds the [GeneratorQrValues] instance after validating the fields.
         *
         * @return A new instance of [GeneratorQrValues].
         * @throws InvalidQrFiled if any of the fields fail validation.
         */
        fun build(): GeneratorQrValues {

            val isValidIdentifier = qrConstraints.identifierConstraint.validate(identifier)
            val isValidAmount = qrConstraints.amountConstraint.validate(amount)
            val isValidExpiry = qrConstraints.expiryConstraint.validate(expiry.formatExpiryDate())

            if (isValidIdentifier.not()) {
                throw InvalidQrFiled(IDENTIFIER)
            } else if (isValidAmount.not()) {
                throw InvalidQrFiled(AMOUNT)
            } else if (isValidExpiry.not()) {
                throw InvalidQrFiled(EXPIRY)
            }

            return GeneratorQrValues(identifier, amount, expiry)
        }
    }

    /**
     * Returns a string representation of the QR code values in URI format.
     *
     * This method formats the identifier, amount, and expiry into a URI string
     * suitable for QR code encoding.
     *
     * @return A formatted string representing the QR code values.
     */
    override fun toString(): String {
        return formatQrUri(
            identifier = identifier,
            amount = amount,
            expiry = expiry
        )
    }
}
