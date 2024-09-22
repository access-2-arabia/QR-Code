package com.a2a.qr_code.generate_qr

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

/**
 * A class for generating QR codes with customizable options such as size, logo, and padding.
 *
 * This class employs a Builder pattern to facilitate the creation of QR codes with specific
 * configurations. It allows for the integration of a logo and provides methods for customizing
 * the QR code's size and appearance.
 */
class QrGenerator private constructor() {

    /**
     * Builder class for constructing QR codes with specified values and customization options.
     *
     * @property values An instance of [GeneratorQrValues] that holds the data to be encoded.
     */
    class Builder(private val values: GeneratorQrValues) {
        private var qrCodeSize: Int = 500
        private var centerSizeScale: Float = 0.2f
        private var paddingScale: Float = 0.2f
        private var logo: Bitmap? = null

        /**
         * Sets the size of the QR code.
         *
         * @param size The desired size in pixels.
         * @return The Builder instance for method chaining.
         */
        fun setQrCodeSize(size: Int) = apply {
            qrCodeSize = size
        }

        /**
         * Sets the padding scale for the logo.
         *
         * @param scale The scale for padding around the logo.
         * @return The Builder instance for method chaining.
         */
        fun setPaddingScale(scale: Float) = apply {
            paddingScale = scale
        }

        /**
         * Sets the logo bitmap to be included in the QR code.
         *
         * @param logoBitmap The bitmap of the logo.
         * @return The Builder instance for method chaining.
         */
        fun setLogo(logoBitmap: Bitmap) = apply {
            logo = logoBitmap
        }

        /**
         * Builds the QR code bitmap based on the specified configurations.
         *
         * @return A bitmap representing the generated QR code, or null if generation fails.
         */
        fun build(): Bitmap? {
            val bitMatrix = generateBitMatrix() ?: return null
            val qrCodeBitmap = createQrCodeBitmap(bitMatrix)
            val maskBitmap = createMaskBitmap()
            val resultBitmap = combineQrCodeAndMask(qrCodeBitmap, maskBitmap)
            return addLogo(resultBitmap)
        }

        /**
         * Generates a BitMatrix for the QR code using the specified values.
         *
         * @return A BitMatrix representing the QR code, or null if an error occurs.
         */
        private fun generateBitMatrix(): BitMatrix? {
            val hints = hashMapOf<EncodeHintType, Any>()
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
            return try {
                MultiFormatWriter().encode(
                    values.toString(),
                    BarcodeFormat.QR_CODE,
                    qrCodeSize,
                    qrCodeSize,
                    hints
                )
            } catch (e: WriterException) {
                e.printStackTrace()
                Log.e("TAG" , e.stackTraceToString())
                null
            }
        }
        /**
         * Creates a bitmap representation of the QR code from the BitMatrix.
         *
         * @param bitMatrix The BitMatrix to convert to a bitmap.
         * @return A bitmap representing the QR code.
         */
        private fun createQrCodeBitmap(bitMatrix: BitMatrix): Bitmap {
            val bitmap = Bitmap.createBitmap(qrCodeSize, qrCodeSize, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val paint = Paint()
            for (x in 0 until qrCodeSize) {
                for (y in 0 until qrCodeSize) {
                    paint.color = if (bitMatrix[x, y]) Color.BLACK else Color.TRANSPARENT
                    canvas.drawPoint(x.toFloat(), y.toFloat(), paint)
                }
            }
            return bitmap
        }

        /**
         * Creates a mask bitmap that will be used to cut out a center portion of the QR code.
         *
         * @return A bitmap representing the mask.
         */
        private fun createMaskBitmap(): Bitmap {
            val centerSize = (qrCodeSize * centerSizeScale).toInt()
            val maskBitmap = Bitmap.createBitmap(qrCodeSize, qrCodeSize, Bitmap.Config.ARGB_8888)
            val maskCanvas = Canvas(maskBitmap)
            val maskPaint = Paint()
            maskPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

            val roundedRect = RectF(
                (qrCodeSize - centerSize) / 2.toFloat(),
                (qrCodeSize - centerSize) / 2.toFloat(),
                (qrCodeSize + centerSize) / 2.toFloat(),
                (qrCodeSize + centerSize) / 2.toFloat()
            )
            val path = Path().apply {
                addRoundRect(
                    roundedRect,
                    (centerSize / 2).toFloat(),
                    (centerSize / 2).toFloat(),
                    Path.Direction.CW
                )
            }
            maskCanvas.drawPath(path, maskPaint)
            return maskBitmap
        }


        /**
         * Combines the QR code bitmap with the mask bitmap to create the final QR code.
         *
         * @param qrCodeBitmap The bitmap of the QR code.
         * @param maskBitmap The bitmap of the mask.
         * @return A bitmap representing the combined result.
         */
        private fun combineQrCodeAndMask(qrCodeBitmap: Bitmap, maskBitmap: Bitmap): Bitmap {
            val resultBitmap = Bitmap.createBitmap(qrCodeSize, qrCodeSize, Bitmap.Config.ARGB_8888)
            val resultCanvas = Canvas(resultBitmap)
            resultCanvas.drawBitmap(qrCodeBitmap, 0f, 0f, null)
            resultCanvas.drawBitmap(maskBitmap, 0f, 0f, null)
            return resultBitmap
        }

        /**
         * Adds the specified logo to the center of the QR code.
         *
         * @param resultBitmap The bitmap of the QR code with the mask applied.
         * @return A bitmap with the logo added, or the original bitmap if no logo is set.
         */
        private fun addLogo(resultBitmap: Bitmap): Bitmap {
            logo?.let {
                val centerSize = (qrCodeSize * centerSizeScale).toInt()
                val logoSizeWithPadding = (centerSize * (1 - paddingScale)).toInt()
                val scaledLogo =
                    Bitmap.createScaledBitmap(it, logoSizeWithPadding, logoSizeWithPadding, false)
                val logoX = (qrCodeSize - logoSizeWithPadding) / 2
                val logoY = (qrCodeSize - logoSizeWithPadding) / 2

                val resultCanvas = Canvas(resultBitmap)
                resultCanvas.drawBitmap(scaledLogo, logoX.toFloat(), logoY.toFloat(), null)
            }
            return resultBitmap
        }
    }
}
