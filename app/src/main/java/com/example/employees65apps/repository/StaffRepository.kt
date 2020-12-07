package com.example.employees65apps.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.employees65apps.database.StaffDao
import com.example.employees65apps.database.asDomainEmployeesWithSpecialties
import com.example.employees65apps.database.asDomainSpecialties
import com.example.employees65apps.domain.EmployeeWithSpecialties
import com.example.employees65apps.domain.Specialty
import com.example.employees65apps.network.StaffApiService
import com.example.employees65apps.network.asDatabaseStaffContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Repository for fetching employees data from the network and storing it in the database
 */
class StaffRepository @Inject constructor(
    private val service: StaffApiService,
    private val staffDao: StaffDao
) {
    /**
     * Get specialties list as LiveData from database, then map to domain
     * @return observable list of specialties
     */
    fun observeSpecialties(): LiveData<List<Specialty>> =
        Transformations.map(staffDao.observeSpecialties()) {
            it.asDomainSpecialties()
        }

    /**
     * Get employees list by specialty ID from database, the map to domain using coroutine
     * @return list of employees having specified specialty
     */
    suspend fun getEmployeesBySpecialty(specialtyId: Long): List<EmployeeWithSpecialties> =
        withContext(Dispatchers.IO) {
            val databaseEmployees = staffDao.getEmployeesWithSpecialtiesBySpecialtyId(specialtyId)
            databaseEmployees.asDomainEmployeesWithSpecialties()
        }

    /**
     * Refresh employees stored in the offline cache
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher
     *
     */
    suspend fun refreshStaff() {
        withContext(Dispatchers.IO) {
            Timber.d("refreshStaff() called")
            val fetchedStaffContainer = service.getContainer()
            val databaseStaffContainer = fetchedStaffContainer.asDatabaseStaffContainer()
            staffDao.insertStaff(databaseStaffContainer)
        }
    }
}