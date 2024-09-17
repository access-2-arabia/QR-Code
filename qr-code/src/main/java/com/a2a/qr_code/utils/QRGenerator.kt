package com.a2a.qr_code.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

/**
 * A class to generate QR codes with optional logo embedding and customization.
 */
class QRGenerator private constructor() {

    /**
     * Builder class for constructing QR codes with customizable options.
     *
     * @property content The text content to be encoded in the QR code.
     */
    class Builder(private val content: String) {
        private var qrCodeSize: Int = 500
        private var centerSizeScale: Float = 0.2f
        private var paddingScale: Float = 0.2f
        private var logo: Bitmap? = null

        /**
         * Sets the size of the QR code.
         *
         * @param size The size in pixels for the QR code.
         * @return The Builder instance for method chaining.
         */
        fun setQrCodeSize(size: Int) = apply {
            qrCodeSize = size
        }

        /**
         * Sets the padding scale for the logo.
         *
         * @param scale The scale factor for the logo padding (between 0 and 1).
         * @return The Builder instance for method chaining.
         */
        fun setPaddingScale(scale: Float) = apply {
            paddingScale = scale
        }

        /**
         * Sets the logo to be embedded in the center of the QR code.
         *
         * @param logoBitmap The Bitmap image of the logo.
         * @return The Builder instance for method chaining.
         */
        fun setLogo(logoBitmap: Bitmap) = apply {
            logo = logoBitmap
        }

        /**
         * Builds the QR code bitmap with the specified configurations.
         *
         * @return The generated QR code as a Bitmap, or null if an error occurs.
         */
        fun build(): Bitmap? {
            val bitMatrix = generateBitMatrix() ?: return null
            val qrCodeBitmap = createQrCodeBitmap(bitMatrix)
            val maskBitmap = createMaskBitmap()
            val resultBitmap = combineQrCodeAndMask(qrCodeBitmap, maskBitmap)
            return addLogo(resultBitmap)
        }

        /**
         * Generates a BitMatrix for the QR code based on the content and size.
         *
         * @return The BitMatrix representing the QR code, or null if an error occurs.
         */
        private fun generateBitMatrix(): BitMatrix? {
            val hints = hashMapOf<EncodeHintType, Any>()
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
            return try {
                MultiFormatWriter().encode(
                    content,
                    BarcodeFormat.QR_CODE,
                    qrCodeSize,
                    qrCodeSize,
                    hints
                )
            } catch (e: WriterException) {
                e.printStackTrace()
                null
            }
        }

        /**
         * Creates a Bitmap representation of the QR code from the BitMatrix.
         *
         * @param bitMatrix The BitMatrix representing the QR code.
         * @return The Bitmap of the QR code.
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
         * Creates a mask Bitmap for the center of the QR code.
         *
         * @return The mask Bitmap with a rounded rectangle in the center.
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
         * Combines the QR code bitmap and mask bitmap.
         *
         * @param qrCodeBitmap The Bitmap of the QR code.
         * @param maskBitmap The Bitmap of the mask.
         * @return The Bitmap combining both the QR code and mask.
         */
        private fun combineQrCodeAndMask(qrCodeBitmap: Bitmap, maskBitmap: Bitmap): Bitmap {
            val resultBitmap = Bitmap.createBitmap(qrCodeSize, qrCodeSize, Bitmap.Config.ARGB_8888)
            val resultCanvas = Canvas(resultBitmap)
            resultCanvas.drawBitmap(qrCodeBitmap, 0f, 0f, null)
            resultCanvas.drawBitmap(maskBitmap, 0f, 0f, null)
            return resultBitmap
        }

        /**
         * Adds the logo to the center of the QR code.
         *
         * @param resultBitmap The Bitmap of the QR code with mask applied.
         * @return The Bitmap with the logo added, or the original bitmap if no logo is set.
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
