package com.example.employees65apps.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Domain class that represents specialty (domain model)
 *
 * See 'database' package for objects that are mapped to the database
 *
 * See 'network' package for objects that parse network calls
 */
@Parcelize
data class Specialty(
    val id: Long,
    val name: String
) : Parcelable