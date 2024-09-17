package com.a2a.qr_code

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.a2a.qr_code.scan_qr.ScanQrContract
import com.a2a.qr_code.scan_qr.ScanQrOption


class MainActivity : AppCompatActivity() {

    private val scanQrLauncher = registerForActivityResult(ScanQrContract()) { qrData ->
        Log.e("Scanned Result", qrData.orEmpty())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.btn)

        btn.setOnClickListener {
            scanQrLauncher.launch(
               ScanQrOption.Builder()
                    .serReadFromGallery(true)
                    .setAutoFocusEnabled(false)
                    .build()
            )
        }

    }

}


