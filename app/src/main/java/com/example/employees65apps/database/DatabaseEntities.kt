package com.example.employees65apps.database

import androidx.room.*
import com.example.employees65apps.domain.EmployeeWithSpecialties
import com.example.employees65apps.domain.Specialty
import org.threeten.bp.LocalDate

/**
 * DatabaseEmployee represents an employee entity in the database (database model)
 */
@Entity(tableName = "employees")
data class DatabaseEmployee(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "first_name")
    val firstName: String,

    @ColumnInfo(name = "last_name")
    val lastName: String,

    val birthday: LocalDate? = null,

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String? = null
)

/**
 * DatabaseSpecialty represents a specialty entity in the database (database model)
 */
@Entity(tableName = "specialties")
data class DatabaseSpecialty(

    @PrimaryKey
    val id: Long,

    val name: String
)

/**
 * DatabaseEmployeeSpecialty represents cross-reference table for many-to-many relationship
 */
@Entity(
    tableName = "employees_specialties",
    primaryKeys = ["employee_id", "specialty_id"],
    indices = [
        Index(value = ["employee_id"]),
        Index(value = ["specialty_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = DatabaseEmployee::class,
            parentColumns = ["id"],
            childColumns = ["employee_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DatabaseSpecialty::class,
            parentColumns = ["id"],
            childColumns = ["specialty_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DatabaseEmployeeSpecialty(

    @ColumnInfo(name = "employee_id")
    val employeeId: Long,

    @ColumnInfo(name = "specialty_id")
    val specialtyId: Long
)

/**
 * DatabaseStaffContainer represents all the data fetched from the network as a database entities (database model)
 */
data class DatabaseStaffContainer(
    val employees: MutableList<DatabaseEmployee> = mutableListOf(),
    val specialties: MutableList<DatabaseSpecialty> = mutableListOf(),
    val employeesToSpecialties: MutableMap<DatabaseEmployee, List<DatabaseSpecialty>> = mutableMapOf()
)

/**
 * DatabaseEmployeeWithSpecialties represents single employee with a list of its specialties (database model)
 */
data class DatabaseEmployeeWithSpecialties(
    @Embedded val employee: DatabaseEmployee,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            DatabaseEmployeeSpecialty::class,
            parentColumn = "employee_id",
            entityColumn = "specialty_id"
        )
    )
    val specialties: List<DatabaseSpecialty>
)

/**
 * Map list of DatabaseSpecialty to domain entities list
 * @return list of domain model entities
 */
fun List<DatabaseSpecialty>.asDomainSpecialties(): List<Specialty> {
    return map {
        Specialty(
            id = it.id,
            name = it.name
        )
    }
}

/**
 * Map list of DatabaseEmployeeWithSpecialties to domain entities
 * @return list of domain model entities
 */
fun List<DatabaseEmployeeWithSpecialties>.asDomainEmployeesWithSpecialties():
        List<EmployeeWithSpecialties> {
    return map {
        EmployeeWithSpecialties(
            id = it.employee.id,
            firstName = it.employee.firstName,
            lastName = it.employee.lastName,
            birthday = it.employee.birthday,
            avatarUrl = it.employee.avatarUrl,
            specialties = it.specialties.map { databaseSpecialty ->
                Specialty(
                    id = databaseSpecialty.id,
                    name = databaseSpecialty.name
                )
            }
        )
    }
}


