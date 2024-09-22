package com.a2a.qr_code.scan_qr

import com.a2a.qr_code.core.qr_constraints.QrConstraints


class ScanQrOption private constructor(
    val fromGallery: Boolean = true,
    val autoFocusEnabled: Boolean = true,
    val qrConstraints: QrConstraints
) {

    class Builder {
        private var autoFocusEnabled: Boolean = true
        private var readFromGallery: Boolean = true
        private var qrConstraints: QrConstraints = QrConstraints.Builder().build()

        fun setAutoFocusEnabled(autoFocusEnabled: Boolean) = apply {
            this.autoFocusEnabled = autoFocusEnabled
        }

        fun serReadFromGallery(fromGallery: Boolean) = apply {
            this.readFromGallery = fromGallery
        }

        fun setQrConstraints(qrConstraints: QrConstraints) = apply {
            this.qrConstraints = qrConstraints
        }

        fun build() = ScanQrOption(
            autoFocusEnabled = autoFocusEnabled,
            fromGallery = readFromGallery,
            qrConstraints = qrConstraints
        )
    }
}


