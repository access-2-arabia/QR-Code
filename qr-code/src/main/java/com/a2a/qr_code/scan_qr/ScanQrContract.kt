package com.a2a.qr_code.scan_qr

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract


/**
 * An [ActivityResultContract] for initiating a QR code scan and returning the scanned QR code data.
 *
 * This contract facilitates launching an activity to scan a QR code using [ScanQRActivity] and
 * retrieving the scanned QR code data as a result. It provides a way to start the scanning activity
 * and handle the result in a structured manner.
 *
 * @constructor Creates an instance of [ScanQrContract] to handle QR code scanning.
 */
class ScanQrContract : ActivityResultContract<ScanQrOption, String?>() {


    /**
     * Creates an [Intent] for launching [ScanQRActivity] with the provided [ScanQrOption].
     *
     * This method configures the [Intent] with the necessary extras to specify whether the scan
     * should be performed from a gallery image and whether auto-focus should be enabled.
     *
     * @param context The [Context] in which the [Intent] will be used.
     * @param input An instance of [ScanQrOption] containing the options for the QR code scan.
     * @return An [Intent] configured to start [ScanQRActivity] with the provided options.
     */
    override fun createIntent(context: Context, input: ScanQrOption): Intent {
        return Intent(context, ScanQRActivity::class.java).apply {
            putExtra("from_gallery", input.fromGallery)
            putExtra("auto_focus", input.autoFocusEnabled)
        }
    }

    /**
     * Parses the result returned from [ScanQRActivity] after the QR code scan is completed.
     *
     * This method extracts the QR code data from the [Intent] if the result is successful
     * (i.e., if the result code is [Activity.RESULT_OK]). If the result code is not OK or the
     * [Intent] is null, it returns `null`.
     *
     * @param resultCode The result code returned by the activity, indicating whether the scan was successful.
     * @param intent The [Intent] returned by [ScanQRActivity] containing the QR code data.
     * @return The QR code data as a [String] if successful; `null` if the result code is not OK or the [Intent] is null.
     */
    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        if (resultCode == Activity.RESULT_OK && intent != null) {
            return intent.getStringExtra(ScanQRActivity.QR_DATA)
        }
        return null
    }
}