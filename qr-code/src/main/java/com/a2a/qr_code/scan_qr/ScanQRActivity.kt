package com.a2a.qr_code.scan_qr

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.a2a.qrCode.databinding.ActivityScanQrBinding
import com.a2a.qr_code.extensions.decodeQRCodeFromUri
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.ScanMode

/**
 * An activity for scanning QR codes using a camera or selecting an image from the gallery.
 *
 * This activity initializes and manages a [CodeScanner] for scanning QR codes with the device camera.
 * It also handles permission requests for camera access and reading from external storage. Users can
 * choose to scan QR codes from gallery images or directly using the camera.
 */
class ScanQRActivity : AppCompatActivity() {

    private val codeScanner: CodeScanner by lazy { initCodeScanner() }
    private lateinit var binding: ActivityScanQrBinding

    /**
     * Callback for handling camera permission result.
     *
     * Requests camera permission and starts the camera preview if permission is granted.
     */
    private val cameraPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
            if (permissionGranted) {
                codeScanner.startPreview()
            }
        }

    /**
     * Callback for handling multiple permissions result.
     *
     * Requests permissions for reading media or external storage, then launches the gallery picker
     * if all permissions are granted.
     */
    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            val allPermissionsGranted = results.all { it.value }
            if (allPermissionsGranted) {
                getImageFromGallery.launch("image/*")
            }
        }

    /**
     * Callback for handling image selection from the gallery.
     *
     * Retrieves the URI of the selected image, decodes it to extract QR code data, and sets the result.
     */
    private val getImageFromGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                setResult(baseContext.decodeQRCodeFromUri(uri).orEmpty())
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanQrBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupOptions()
        binding.imgGallery.setOnClickListener { checkReadExternalPermission() }
    }

    private fun setupOptions() {
        val fromGallery = intent.getBooleanExtra(FROM_GALLERY, false)
        val autoFocusEnabled = intent.getBooleanExtra(AUTO_FOCUS, true)
        binding.imgGallery.isVisible = fromGallery
        binding.scannerView.isAutoFocusButtonVisible= autoFocusEnabled
    }

    override fun onStart() {
        super.onStart()
        cameraPermissionResult.launch(Manifest.permission.CAMERA)
        codeScanner.setDecodeCallback {
            setResult(it.text)
        }
    }

    /**
     * Sets the result for the activity and finishes it.
     *
     * @param data The QR code data to be returned as the result of the activity.
     */
    private fun setResult(data: String) {
        val resultIntent = Intent().apply { putExtra(QR_DATA, data) }
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    /**
     * Checks and requests permissions required to read from external storage.
     */
    private fun checkReadExternalPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VISUAL_USER_SELECTED))
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES))
        } else {
            requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
        }
    }

    /**
     * Initializes the [CodeScanner] with default settings.
     *
     * @return An instance of [CodeScanner] configured for QR code scanning.
     */
    private fun initCodeScanner(): CodeScanner {
        return CodeScanner(baseContext, binding.scannerView).apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
        }
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraPermissionResult.unregister()
    }

    companion object {
        /**
         * Key for passing QR code data in the result intent.
         */
        const val QR_DATA = "qr_data"
        const val FROM_GALLERY = "from_gallery"
        const val AUTO_FOCUS = "auto_focus"
    }

}