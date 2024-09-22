package com.a2a.qr_code

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.a2a.qr_code.core.qr_constraints.QrConstraints
import com.a2a.qr_code.core.qr_constraints.defaults.DefaultAmountFieldConstraint
import com.a2a.qr_code.core.qr_constraints.defaults.DefaultExpiryConstraint
import com.a2a.qr_code.core.qr_constraints.defaults.DefaultIdentifierConstraint
import com.a2a.qr_code.generate_qr.GeneratorQrValues
import com.a2a.qr_code.generate_qr.QrGenerator
import com.a2a.qr_code.scan_qr.ScanQrContract
import com.a2a.qr_code.scan_qr.ScanQrOption


class MainActivity : AppCompatActivity() {

    private val scanQrLauncher = registerForActivityResult(ScanQrContract()) { qrData ->
        Log.e("Scanned Result", qrData.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Usage example for generating and scanning a QR code

       // 1. Initialize the ImageView for displaying the generated QR code
        val imgQr = findViewById<ImageView>(R.id.img_qr_code)

       // 2. Create constraints for the QR code fields using the builder pattern
        val qrConstraints = QrConstraints.Builder()
            .setIdentifierConstraint(DefaultIdentifierConstraint()) // Set identifier constraint
            .setAmountConstraint(DefaultAmountFieldConstraint()) // Set amount constraint
            .setExpiryConstraint(DefaultExpiryConstraint()) // Set expiry constraint
            .build()

       // 3. Create QR code values using the builder pattern
        val qrValues = GeneratorQrValues.Builder()
            .setIdentifier("123") // Set the identifier for the QR code
            .setAmount("1.0") // Set the amount associated with the QR code
            .setExpiry("2/10/2024") // Set the expiry date for the QR code
            .setQrConstraints(qrConstraints) // Attach the constraints to the QR values
            .build()

        // 4. Load a logo bitmap to include in the QR code (if desired)
        val logoBitmap = BitmapFactory.decodeResource(resources, R.drawable.whatsapp_logo)

        // 5. Generate the QR code bitmap using the generator class
        val qrCodeBitmap = QrGenerator.Builder(qrValues)
            .setLogo(logoBitmap) // Optional: Set the logo bitmap
            .setPaddingScale(0.0f) // Set padding scale for the QR code
            .build()

        // 6. Display the generated QR code in the ImageView
        imgQr.setImageBitmap(qrCodeBitmap)

        // 7. Set up the button to initiate QR code scanning
        val btn = findViewById<Button>(R.id.btn)
        btn.setOnClickListener {
            // Launch the QR scanner with options to read from the gallery and validate with constraints
            scanQrLauncher.launch(
                ScanQrOption.Builder()
                    .serReadFromGallery(true) // Allow reading from the gallery
                    .setQrConstraints(qrConstraints) // Set the same constraints for validation during scanning
                    .build()
            )
        }
    }

}


