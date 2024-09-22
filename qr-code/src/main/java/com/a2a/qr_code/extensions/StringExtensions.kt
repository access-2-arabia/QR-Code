package com.a2a.qr_code.extensions

import android.net.Uri
import com.a2a.qr_code.core.AMOUNT
import com.a2a.qr_code.core.EXPIRY
import com.a2a.qr_code.core.IDENTIFIER
import com.a2a.qr_code.core.IS_VALID_QR_URI_REGEX
import com.a2a.qr_code.exceptions.InvalidQrException
import com.a2a.qr_code.model.QRResult


internal fun String.readQr(): QRResult {
    val regex = Regex(IS_VALID_QR_URI_REGEX)
    val qrData = Uri.decode(this)
    if (qrData.matches(regex)) {
        val uri = Uri.parse(qrData)
        return QRResult(
            identifier = uri.getQueryParameter(IDENTIFIER),
            amount = uri.getQueryParameter(AMOUNT),
            expiry = uri.getQueryParameter(EXPIRY)
        )
    } else
        throw InvalidQrException()
}