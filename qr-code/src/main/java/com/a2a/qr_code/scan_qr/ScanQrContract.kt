package com.a2a.qr_code.scan_qr

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.a2a.qr_code.model.QRResult



class ScanQrContract : ActivityResultContract<ScanQrOption, QRResult?>() {


    override fun createIntent(context: Context, input: ScanQrOption): Intent {
        return Intent(context, ScanQRActivity::class.java).apply {
            putExtra(ScanQRActivity.FROM_GALLERY, input.fromGallery)
            putExtra(ScanQRActivity.AUTO_FOCUS, input.autoFocusEnabled)
            putExtra(ScanQRActivity.CONSTRAINTS , input.qrConstraints)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): QRResult? {
        if (resultCode == Activity.RESULT_OK && intent != null) {
            @Suppress("DEPRECATION")
            return intent.getParcelableExtra(ScanQRActivity.QR_DATA)
        }
        return null
    }

}