package com.example.employees65apps.network

import com.example.employees65apps.database.DatabaseEmployee
import com.example.employees65apps.database.DatabaseSpecialty
import com.example.employees65apps.database.DatabaseStaffContainer
import com.example.employees65apps.utils.parseNetworkDate
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

/**
 * NetworkStaffContainer class holds a list of NetworkEmployees (network model)
 *
 * Necessary to parse the first level of network response
 *
 * {
 *   "response": []
 * }
 */
@JsonClass(generateAdapter = true)
data class NetworkStaffContainer(
    @Json(name = "response") val employees: List<NetworkEmployee>
)

/**
 * NetworkEmployee class is necessary to parse employee information from network response (network model)
 */
@JsonClass(generateAdapter = true)
data class NetworkEmployee(
    @Json(name = "f_name") val firstName: String,
    @Json(name = "l_name") val lastName: String,
    val birthday: String?,
    @Json(name = "avatr_url") val photoUrl: String?,
    @Json(name = "specialty") val specialties: List<NetworkSpecialty>
)

/**
 * NetworkSpecialty class is necessary to parse employee specialty (network model)
 */
@JsonClass(generateAdapter = true)
data class NetworkSpecialty(
    @Json(name = "specialty_id") val specialtyId: Long,
    val name: String
)

/**
 * Map NetworkStaffContainer to database container. Convert network response to database model
 * @return database model of container
 */
fun NetworkStaffContainer.asDatabaseStaffContainer(): DatabaseStaffContainer {
    val databaseStaffContainer = DatabaseStaffContainer()
    val databaseSpecialtiesSet: MutableSet<DatabaseSpecialty> = mutableSetOf()

    // For each employee in container
    employees.forEach {
        // Convert NetworkEmployee to DatabaseEmployee
        val employee = DatabaseEmployee(
            firstName = it.firstName.toLowerCase(Locale.ROOT).capitalize(Locale.ROOT),
            lastName = it.lastName.toLowerCase(Locale.ROOT).capitalize(Locale.ROOT),
            birthday = parseNetworkDate(it.birthday),
            avatarUrl = if (!it.photoUrl.isNullOrEmpty()) it.photoUrl else null
        )
        databaseStaffContainer.employees.add(employee)

        // Get NetworkEmployee specialties, convert to database specialties and add to HashSet to
        // collect a set of unique specialties
        val employeeSpecialties = it.specialties.map { networkSpecialty ->
            DatabaseSpecialty(networkSpecialty.specialtyId, networkSpecialty.name)
        }
        databaseSpecialtiesSet.addAll(employeeSpecialties)

        // Add employee and list of its specialties to Map
        databaseStaffContainer.employeesToSpecialties[employee] = employeeSpecialties
    }
    databaseStaffContainer.specialties.addAll(databaseSpecialtiesSet)
    return databaseStaffContainer
}

//"response" : [
//{
//    "f_name" 	: "иВан",
//    "l_name" 	: "ИваноВ",
//    "birthday"	: "1987-03-23",
//    "avatr_url"	: "https://2.cdn.echo.msk.ru/files/avatar2/2561900.jpg",
//    "specialty" : [{
//    "specialty_id" : 101,
//    "name"	: "Менеджер"
//}]
//},
//...
