# QR Code Library - Syria

## Overview

The QR Code Library offers a user-friendly solution for generating and decoding QR codes in Android applications, making it particularly valuable for developers. This library allows you to create customizable QR codes tailored to specific needs, facilitating easy sharing of information. Additionally, it supports QR code scanning, enabling users to quickly access content or services. With its straightforward integration and versatile features, the QR Code Library can enhance various applications, from business transactions to educational tools, promoting digital engagement in Syria.
## Features

- Generate QR codes with customizable size, padding, and logo.
- Scan QR codes using device cameras via `ScanQRActivity`.
- Select images from the gallery and decode QR codes from them.

## Table of Contents

1. [Setup](#setup)
    - [Add Repositories](#add-repositories)
    - [Library Dependencies](#library-dependencies)
2. [Usage](#usage)
    - [QR Code Generation](#qr-code-generation)
    - [QR Code Scanning](#qr-code-scanning)
    - [Adding Custom Constraints](#adding-custom-constraints)
3. [API Reference](#api-reference)

## Setup

To use the QR Code Library in your Android project, follow these steps:

### Add Repositories

Ensure you have the required repositories included in your `build.gradle` file (project-level):

```groovy
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url "https://jitpack.io" }
    }
}
```

### Library Dependencies

Add the necessary dependencies to your `build.gradle` file (app-level):

```kotlin
dependencies {
    implementation ("com.github.access-2-arabia:QR-Code:1.0.0-alpha")
}
```

## Usage

### QR Code Generation

To generate a QR code, use the `QrGenerator` class:

```kotlin
val qrConstraints = QrConstraints.Builder()
    .setIdentifierConstraint(DefaultIdentifierConstraint())
    .setAmountConstraint(DefaultAmountFieldConstraint())
    .setExpiryConstraint(DefaultExpiryConstraint())
    .build()

val qrValues = GeneratorQrValues.Builder()
    .setIdentifier("123")
    .setAmount("1.0")
    .setExpiry(Duration(days = 2))
    .setQrConstraints(qrConstraints)
    .build()

val logoBitmap = BitmapFactory.decodeResource(resources, R.drawable.logo)

val qrCodeBitmap = QrGenerator.Builder(qrValues)
    .setLogo(logoBitmap)
    .setPaddingScale(0.0f)
    .setQrCodeSize(500)
    .build()
```

### QR Code Scanning

To scan QR codes, use `ScanQRActivity` directly in your application:

```kotlin
val qrConstraints = QrConstraints.Builder()
    .setIdentifierConstraint(DefaultIdentifierConstraint())
    .setAmountConstraint(DefaultAmountFieldConstraint())
    .setExpiryConstraint(DefaultExpiryConstraint())
    .build()

val intent = Intent(this, ScanQRActivity::class.java).apply {
    putExtra(ScanQRActivity.FROM_GALLERY, true)
    putExtra(ScanQRActivity.CONSTRAINTS, qrConstraints)
}
startActivityForResult(intent, REQUEST_CODE_SCAN_QR)
```

### Adding Custom Constraints

You can create your own constraints by implementing the `QrConstraint` interface. Here’s how to define a custom constraint:

1. **Create Your Custom Constraint Class**:

```kotlin
class CustomIdentifierConstraint : QrFiledConstraint<String>  {
    override fun isValid(value: String): Boolean {
        // Implement your custom validation logic here
        return value.length == 6 // Example: must be exactly 6 characters
    }
}
```

2. **Add Your Custom Constraint**:

In the `QrConstraints.Builder`, replace the default constraints with your custom ones:

```kotlin
val qrConstraints = QrConstraints.Builder()
    .setIdentifierConstraint(CustomIdentifierConstraint())
    .setAmountConstraint(DefaultAmountFieldConstraint())
    .setExpiryConstraint(DefaultExpiryConstraint())
    .build()
```

This allows you to enforce your own rules for QR code validation.

### `ScanQRActivity`

The `ScanQRActivity` is part of the library and is responsible for scanning and decoding QR codes using the device's camera and allowing users to select images from their gallery.

#### Features

- **Camera Access**: Requests permission to access the camera for scanning QR codes.
- **Gallery Access**: Lets users select an image from the gallery to decode a QR code.
- **QR Code Validation**: Validates the scanned or selected QR code against predefined constraints.
- **User Notifications**: Displays error dialogs for invalid QR codes or permission-related issues.

#### Permissions

`ScanQRActivity` requires the following permissions:

- `CAMERA`: For scanning QR codes using the camera.
- `READ_EXTERNAL_STORAGE` or `READ_MEDIA_IMAGES`: For accessing images from the gallery.

#### Intent Extras

- `FROM_GALLERY`: Indicates whether to allow image selection from the gallery.
- `AUTO_FOCUS`: Determines if the auto-focus button is visible in the scanner view.
- `CONSTRAINTS`: A Parcelable object defining validation rules for the QR code fields.

#### QR Code Data

The QR code data must include the following fields:

- `identifier`: This represents a unique identifier for the QR code, which could be a card number, account number, or any other specific reference. For example, in a payment application, the identifier might be the user’s account number
- `amount`: The amount associated with the QR code.
- `expiry`: The expiry date for the QR code.

If any fields do not meet validation criteria, an `InvalidQrField` exception will be thrown.

#### Error Handling

If an invalid QR code is detected or issues arise during scanning, an error dialog will prompt the user to use a valid QR code.

## API Reference

### `QrGenerator`

- **Builder Class**
    - `setQrCodeSize(size: Int)`: Set the size of the QR code.
    - `setPaddingScale(scale: Float)`: Set the padding scale for the logo.
    - `setLogo(logoBitmap: Bitmap)`: Set the logo to be embedded in the QR code.
    - `setQrConstraints(qrConstraints: QrConstraints)`: Set constraints for the QR code.
    - `build()`: Generate the QR code bitmap with the specified options.

### `ScanQrOption`

- **Builder Class**
    - `setAutoFocusEnabled(autoFocusEnabled: Boolean)`: Set whether auto-focus is enabled.
    - `setReadFromGallery(fromGallery: Boolean)`: Set whether the QR code should be read from the gallery.
    - `setQrConstraints(qrConstraints: QrConstraints)`: Set the constraints for scanning the QR code.
    - `build()`: Build the `ScanQrOption` instance.

### `ScanQRActivity`

- **Constants**
    - `QR_DATA`: Key for the scanned QR code data.
    - `FROM_GALLERY`: Key to indicate if the activity allows image selection.
    - `AUTO_FOCUS`: Key for enabling auto-focus in the scanner view.
    - `CONSTRAINTS`: Key for validation constraints.

### `ScanQrContract`

- **createIntent(context: Context, input: ScanQrOption): Intent**
- **parseResult(resultCode: Int, intent: Intent?): String?**