package com.a2a.qr_code.core.qr_constraints

import android.os.Parcelable

/**
 * An interface representing a constraint for QR code fields.
 *
 * This interface defines a contract for implementing various constraints that can be applied
 * to different types of QR code field values. Any class implementing this interface should
 * provide the logic for validating the corresponding field value based on specific criteria.
 *
 * @param T The type of the value that the constraint will validate.
 */
interface QrFiledConstraint<T> : Parcelable {

    /**
     * Validates the provided value against the constraint.
     *
     * @param value The value to be validated. It can be null.
     * @return `true` if the value meets the constraint criteria; `false` otherwise.
     */
    fun validate(value: T?): Boolean
}