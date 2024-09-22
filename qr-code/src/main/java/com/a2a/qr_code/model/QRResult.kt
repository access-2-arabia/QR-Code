package com.a2a.qr_code.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * A data class representing the result of decoding a QR code.
 *
 * This class holds the parsed values from a QR code, including the identifier, amount, and expiry date.
 *
 * @property identifier The unique identifier extracted from the QR code, or null if not present.
 * @property amount The amount associated with the QR code, or null if not present.
 * @property expiry The expiry date associated with the QR code, or null if not present.
 */
@Parcelize
data class QRResult(
    val identifier: String?,
    val amount: String?,
    val expiry:String?
):Parcelable
