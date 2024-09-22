package com.a2a.qr_code.core

import android.net.Uri

internal fun formatQrUri(identifier: String, amount: String, expiry: String): String {
    return Uri.encode(
        Uri.Builder()
            .scheme(PAYMENT)
            .authority("")
            .appendQueryParameter(IDENTIFIER, identifier)
            .appendQueryParameter(AMOUNT, amount)
            .appendQueryParameter(EXPIRY, expiry)
            .build()
            .toString()
    )
}
