package com.a2a.qr_code.extensions

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri

/**
 * Decodes a QR code from an image specified by a [Uri].
 *
 * This function retrieves the image from the provided [Uri], decodes it into a [Bitmap],
 * and then uses the [Bitmap.readQrCode] extension function to extract and return the QR code
 * text.
 *
 * @param imageUri The [Uri] pointing to the image file from which the QR code is to be decoded.
 * @return The decoded QR code text if successful; `null` if the image cannot be accessed,
 *         decoded, or if the QR code cannot be read.
 */
fun Context.decodeQRCodeFromUri(imageUri: Uri): String? {
    val inputStream = contentResolver.openInputStream(imageUri)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    return bitmap.readQrCode()
}
