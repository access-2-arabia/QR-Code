package com.a2a.qr_code.exceptions

/**
 * Exception thrown when a specific QR code field is found to be invalid.
 *
 * This custom exception is used to indicate that a particular field in the QR code does not
 * meet the required criteria for validity, allowing for more detailed error reporting.
 *
 * @property fieldName The name of the invalid field, which will be included in the error message.
 */
class InvalidQrFiled(private val fieldName: String?) : Exception() {

    override val message: String
        get() = "The $fieldName field is invalid"
}