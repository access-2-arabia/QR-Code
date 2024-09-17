package com.a2a.qr_code.extensions

import android.graphics.Bitmap
import com.google.zxing.BinaryBitmap
import com.google.zxing.LuminanceSource
import com.google.zxing.MultiFormatReader
import com.google.zxing.ReaderException
import com.google.zxing.common.HybridBinarizer

/**
 * Reads and decodes a QR code from the given [Bitmap].
 *
 * This function converts the `Bitmap` to a grayscale image, then uses the ZXing library to decode
 * the QR code from the grayscale image. It is suitable for decoding QR codes from images that
 * are in the form of `Bitmap` objects.
 *
 * @return The decoded QR code text if successful; `null` if decoding fails or the input
 *         does not contain a valid QR code.
 */
fun Bitmap.readQrCode(): String? {
    val bitmap = this.copy(Bitmap.Config.ARGB_8888, true)

    val intArray = IntArray(bitmap.width * bitmap.height)
    bitmap.getPixels(intArray, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

    val source = object : LuminanceSource(bitmap.width, bitmap.height) {
        override fun getRow(y: Int, row: ByteArray?): ByteArray {
            val rowArray = row ?: ByteArray(width)
            for (x in 0 until width) {
                val pixel = intArray[y * width + x]
                val gray = ((pixel shr 16 and 0xFF) * 0.299 + (pixel shr 8 and 0xFF) * 0.587 + (pixel and 0xFF) * 0.114).toInt()
                rowArray[x] = gray.toByte()
            }
            return rowArray
        }

        override fun getMatrix(): ByteArray {
            val matrix = ByteArray(width * height)
            for (y in 0 until height) {
                val row = getRow(y, null)
                System.arraycopy(row, 0, matrix, y * width, width)
            }
            return matrix
        }
    }

    val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
    val reader = MultiFormatReader()
    return try {
        val result = reader.decode(binaryBitmap)
        result.text
    } catch (e: ReaderException) {
        null
    }
}