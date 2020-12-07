package com.example.employees65apps.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.threeten.bp.LocalDate
import org.threeten.bp.Period

/**
 * Domain class that represents employee with a list of its specialties (domain model)
 *
 * See 'database' package for objects that are mapped to the database
 *
 * See 'network' package for objects that parse network calls
 */
@Parcelize
data class EmployeeWithSpecialties(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val birthday: LocalDate?,
    val avatarUrl: String?,
    val specialties: List<Specialty>
) : Parcelable {
    val age: Int?
        get() = if (birthday != null) {
            Period.between(birthday, LocalDate.now()).years
        } else {
            null
        }
}
