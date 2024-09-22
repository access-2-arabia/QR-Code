package com.a2a.qr_code.scan_qr

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.a2a.qrCode.databinding.ActivityScanQrBinding
import com.a2a.qr_code.core.qr_constraints.QrConstraints
import com.a2a.qr_code.exceptions.InvalidQrFiled
import com.a2a.qr_code.extensions.decodeQRCodeFromUri
import com.a2a.qr_code.extensions.readQr
import com.a2a.qr_code.model.QRResult
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.ScanMode


/**
 * ScanQRActivity is an Android Activity responsible for scanning and decoding QR codes
 * using the device's camera, as well as allowing users to select images from their gallery
 * for QR code decoding.
 *
 * ## Features
 * - **Camera Access**: Requests permission to access the device's camera for scanning QR codes.
 * - **Gallery Access**: Allows users to select an image from the gallery to decode a QR code.
 * - **QR Code Validation**: Validates the scanned or selected QR code against predefined constraints.
 * - **User Notifications**: Displays error dialogs for invalid QR codes or permission-related issues.
 *
 * ## Activity Lifecycle
 * - **onCreate**: Initializes the UI, sets up options based on intent extras, and handles clicks for gallery access.
 * - **onStart**: Requests camera permissions and initializes the QR code scanning process.
 * - **onPause**: Releases camera resources to prevent resource leaks when the activity is not in the foreground.
 * - **onDestroy**: Unregisters the camera permission result callback to avoid memory leaks.
 *
 * ## Permissions
 * This activity requires the following permissions:
 * - `CAMERA`: Required for scanning QR codes using the camera.
 * - `READ_EXTERNAL_STORAGE` or `READ_MEDIA_IMAGES`: Required for accessing images from the user's gallery.
 *
 * ## Intent Extras
 * - `FROM_GALLERY`: A boolean indicating whether the activity should allow image selection from the gallery.
 * - `AUTO_FOCUS`: A boolean that determines if the auto-focus button should be visible in the scanner view.
 * - `CONSTRAINTS`: A Parcelable object of type `QrConstraints` that defines validation rules for the QR code fields.
 *
 * ## QR Code Data
 * The QR code data must include the following fields:
 * - `identifier`: The unique identifier for the QR code.
 * - `amount`: The amount associated with the QR code.
 * - `expiry`: The expiry date for the QR code.
 *
 * If any of these fields do not meet the validation criteria, an `InvalidQrFiled` exception will be thrown.
 *
 * ## Error Handling
 * If an invalid QR code is detected or if there are issues during the scanning process, an error dialog will prompt the user
 * to use a valid QR code.
 *
 * ## Usage Example
 * ```kotlin
 * val intent = Intent(this, ScanQRActivity::class.java).apply {
 *     putExtra(ScanQRActivity.FROM_GALLERY, true)
 *     putExtra(ScanQRActivity.CONSTRAINTS, qrConstraints)
 * }
 * startActivityForResult(intent, REQUEST_CODE_SCAN_QR)
 * ```
 */
class ScanQRActivity : AppCompatActivity() {

    private val codeScanner: CodeScanner by lazy { initCodeScanner() }
    private lateinit var binding: ActivityScanQrBinding

    private val cameraPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
            if (permissionGranted) {
                codeScanner.startPreview()
            }
        }

    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            val allPermissionsGranted = results.all { it.value }
            if (allPermissionsGranted) {
                getImageFromGallery.launch("image/*")
            }
        }

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
        binding.scannerView.isAutoFocusButtonVisible = autoFocusEnabled
    }

    override fun onStart() {
        super.onStart()
        cameraPermissionResult.launch(Manifest.permission.CAMERA)
        codeScanner.setDecodeCallback {
            setResult(it.text)
        }
    }


    private fun setResult(data: String) {
        try {
            val qrResult = data.readQr()
            validateQrFields(qrResult)
            val resultIntent = Intent().apply { putExtra(QR_DATA, qrResult) }
            setResult(RESULT_OK, resultIntent)
            finish()
        } catch (e: Exception) {
            Log.e("TAG", e.stackTraceToString())
            runOnUiThread { showDialog() }
        }
    }

    /**
     * Validates the fields of the provided QR code result against predefined constraints.
     *
     * This function checks if the `identifier`, `amount`, and `expiry` fields of the
     * `QRResult` object meet the specified validation criteria defined in the
     * `QrConstraints` object retrieved from the intent.
     *
     * @param result The `QRResult` object containing the fields to validate:
     * - `identifier`: A unique identifier for the QR code.
     * - `amount`: The amount associated with the QR code.
     * - `expiry`: The expiry date of the QR code.
     *
     * @throws InvalidQrFiled If any of the fields are invalid according to the constraints:
     * - Throws an `InvalidQrFiled` exception with the field name if the identifier is invalid.
     * - Throws an `InvalidQrFiled` exception with the field name if the amount is invalid.
     * - Throws an `InvalidQrFiled` exception with the field name if the expiry is invalid.
     *
     * The function retrieves the `QrConstraints` from the intent using the key `CONSTRAINTS`.
     * If the constraints are not available, the function returns early without performing validation.
     */
    private fun validateQrFields(result: QRResult) {
        @Suppress("DEPRECATION")
        val qrConstraints: QrConstraints = intent.getParcelableExtra(CONSTRAINTS) ?: return
        val identifier = result.identifier
        val amount = result.amount
        val expiry = result.expiry
        val isValidIdentifier = qrConstraints.identifierConstraint.validate(identifier)
        val isValidAmount = qrConstraints.amountConstraint.validate(amount)
        val isValidExpiry = qrConstraints.expiryConstraint.validate(expiry)

        if (isValidIdentifier.not()) {
            throw InvalidQrFiled("identifier")
        } else if (isValidAmount.not()) {
            throw InvalidQrFiled("amount")
        } else if (isValidExpiry.not()) {
            throw InvalidQrFiled("expiry")
        }
    }

    private fun checkReadExternalPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VISUAL_USER_SELECTED))
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES))
        } else {
            requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
        }
    }

    private fun initCodeScanner(): CodeScanner {
        return CodeScanner(baseContext, binding.scannerView).apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
        }
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        val dialog = builder.setTitle("Invalid QR")
            .setMessage("Please use valid qr")
            .setPositiveButton("OK") { _, _ -> codeScanner.startPreview() }
            .create()

        dialog.show()
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
        const val QR_DATA = "qr_data"
        const val FROM_GALLERY = "from_gallery"
        const val AUTO_FOCUS = "auto_focus"
        const val CONSTRAINTS = "constraints"
    }

}