package com.a2a.qr_code.scan_qr

/**
 * Represents options for configuring the QR code scanning process.
 *
 * This class provides options to control whether the QR code should be scanned from
 * a gallery image and whether auto-focus should be enabled during scanning.
 *
 * @property fromGallery Indicates if the QR code should be scanned from an image in the gallery.
 * @property autoFocusEnabled Indicates if auto-focus should be enabled during the scan.
 *
 * @constructor Creates a [ScanQrOption] instance with the specified options.
 */
class ScanQrOption private constructor(
    val fromGallery: Boolean = true,
    val autoFocusEnabled: Boolean = true
) {

    /**
     * A builder class for constructing [ScanQrOption] instances.
     *
     * This builder class allows for a fluent API to configure and build a [ScanQrOption] object
     * with desired settings.
     */
    class Builder {
        private var autoFocusEnabled: Boolean = true
        private var readFromGallery: Boolean = true

        /**
         * Sets whether auto-focus should be enabled during QR code scanning.
         *
         * @param autoFocusEnabled A boolean indicating if auto-focus should be enabled.
         * @return The current [Builder] instance for chaining.
         */
        fun setAutoFocusEnabled(autoFocusEnabled: Boolean) = apply {
            this.autoFocusEnabled = autoFocusEnabled
        }

        /**
         * Sets whether the QR code should be read from a gallery image.
         *
         * @param fromGallery A boolean indicating if the QR code should be read from a gallery image.
         * @return The current [Builder] instance for chaining.
         */
        fun serReadFromGallery(fromGallery: Boolean) = apply {
            this.readFromGallery = fromGallery
        }


        /**
         * Builds a [ScanQrOption] instance with the configured options.
         *
         * @return A [ScanQrOption] instance with the specified options.
         */
        fun build() = ScanQrOption(
            autoFocusEnabled = autoFocusEnabled,
            fromGallery = readFromGallery
        )
    }
}


