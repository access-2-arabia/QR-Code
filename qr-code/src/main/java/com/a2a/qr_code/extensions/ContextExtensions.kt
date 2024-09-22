package com.a2a.qr_code.extensions

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri

/**
 * Extension function to decode a QR code from an image URI.
 *
 * This function takes a URI pointing to an image, retrieves the image as a Bitmap,
 * and attempts to decode any QR code present in the Bitmap. If decoding is successful,
 * it returns the QR code text as a String; otherwise, it returns null.
 *
 * @param imageUri The URI of the image to be processed.
 * @return The decoded QR code text as a String, or null if the decoding fails or the image is invalid.
 */
fun Context.decodeQRCodeFromUri(imageUri: Uri): String? {
    val inputStream = contentResolver.openInputStream(imageUri)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    return bitmap.readQrCode()
}
