# QR Code Library

## Overview

The QR Code Library provides an easy way to generate and decode QR codes in your Android applications. It includes functionality for creating QR codes with customizable options, handling QR code scanning, and more.

## Features

- Generate QR codes with customizable size, padding, and logo.
- Scan QR codes using device cameras.
- Select images from the gallery and decode QR codes from them.

## Table of Contents

1. [Setup](#setup)
    - [Add Repositories](#add-repositories)
    - [Library Dependencies](#library-dependencies)
2. [Usage](#usage)
    - [QR Code Generation](#qr-code-generation)
    - [QR Code Scanning](#qr-code-scanning)
3. [API Reference](#api-reference)

## Setup

To use the QR Code Library in your Android project, follow these steps:

### Add Repositories

Ensure you have the required repositories included in your `build.gradle` file (project-level). This includes the Google Maven repository and JitPack for additional library support.

In your root `build.gradle` file:

```groovy
allprojects {
    repositories {
        google()      // Essential for Jetpack libraries and other Android dependencies
        mavenCentral() // Central repository for other dependencies
        maven { url "https://jitpack.io" } // JitPack repository for hosted libraries
    }
}
```

### Library Dependencies

Add the necessary dependencies to your `build.gradle` file (app-level). Ensure these are included as needed for your project:

```kotlin
dependencies {
    implementation ("com.a2a.qrCode:1.0.0")
}
```

## Usage

### QR Code Generation

To generate a QR code, use the `QRGenerator` class and its `Builder`. Here is an example:

```kotlin
val qrCodeBitmap = QRGenerator.Builder("Your QR Code Content")
    .setQrCodeSize(500)
    .setPaddingScale(0.2f)
    .setLogo(yourLogoBitmap) // Optional
    .build()
```

### QR Code Scanning

To scan QR codes, create an instance of `ScanQRActivity` and use the `ScanQrContract` to handle the result:

1. **Create the Contract**

   ```kotlin
   val scanQrOption = ScanQrOption.Builder()
       .setAutoFocusEnabled(true)
       .setReadFromGallery(false)
       .build()
   ```

2. **Launch the Activity**

   ```kotlin
   val scanQrLauncher = registerForActivityResult(ScanQrContract()) { qrData ->
       // Handle the QR code result
   }
   scanQrLauncher.launch(scanQrOption)
   ```

## API Reference

### `QRGenerator`

- **Builder Class**

    - `setQrCodeSize(size: Int)`: Set the size of the QR code.
    - `setPaddingScale(scale: Float)`: Set the padding scale for the logo.
    - `setLogo(logoBitmap: Bitmap)`: Set the logo to be embedded in the QR code.
    - `build()`: Generate the QR code bitmap with the specified options.

### `ScanQrOption`

- **Builder Class**

    - `setAutoFocusEnabled(autoFocusEnabled: Boolean)`: Set whether auto focus is enabled.
    - `setReadFromGallery(fromGallery: Boolean)`: Set whether the QR code should be read from the gallery.
    - `build()`: Build the `ScanQrOption` instance.

### `ScanQrContract`

- **createIntent(context: Context, input: ScanQrOption): Intent**
- **parseResult(resultCode: Int, intent: Intent?): String?**

---

