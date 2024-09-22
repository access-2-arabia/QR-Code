package com.a2a.qr_code.exceptions

/**
 * Exception thrown when a QR code is found to be invalid.
 *
 * This custom exception is used to indicate that the QR code does not meet the required
 * criteria for validity, allowing for more informative error handling in the application.
 */
class InvalidQrException : Exception() {

    override val message: String
        get() = "The QR code is invalid"

}