package com.example.employees65apps.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface StaffDao {
    /**
     * Insert a list of DatabaseEmployee to database
     * @param employees list of DatabaseEmployee
     * @return inserted employee's ids
     */
    @Insert
    suspend fun insertEmployees(employees: List<DatabaseEmployee>): List<Long>

    /**
     * Insert a list of DatabaseSpecialty to database
     * @param specialties list of DatabaseSpecialty
     */
    @Insert
    suspend fun insertSpecialties(specialties: List<DatabaseSpecialty>)

    /**
     * Observe a list of DatabaseSpecialty from database
     * @return LiveData containing list of DatabaseSpecialty
     */
    @Query("select * from specialties order by name")
    fun observeSpecialties(): LiveData<List<DatabaseSpecialty>>

    /**
     * Insert a list of DatabaseEmployeeSpecialty to database
     * @param employeesSpecialties list of DatabaseEmployeeSpecialty
     */
    @Insert
    suspend fun insertEmployeesSpecialties(employeesSpecialties: List<DatabaseEmployeeSpecialty>)

    /**
     * Delete all rows from 'employees' table
     */
    @Query("delete from employees")
    suspend fun deleteEmployees()

    /**
     * Delete all rows from 'specialties' table
     */
    @Query("delete from specialties")
    suspend fun deleteSpecialties()

    /**
     * Delete all rows from 'employees_specialties' table
     */
    @Query("delete from employees_specialties")
    suspend fun deleteEmployeesSpecialties()

    /**
     * Get list of DatabaseEmployeeWithSpecialties by specialty ID from database
     * @param specialtyId id of specialty
     * @return list of DatabaseEmployeeWithSpecialties
     */
    @Transaction
    @Query(
        """
        select
            e.id,
            e.first_name,
            e.last_name,
            e.birthday,
            e.avatar_url
        from employees_specialties es 
        inner join employees e
            on e.id = es.employee_id  
        where es.specialty_id = :specialtyId
        order by e.last_name
        """
    )
    suspend fun getEmployeesWithSpecialtiesBySpecialtyId(specialtyId: Long): List<DatabaseEmployeeWithSpecialties>

    /**
     * Insert all the data fetched from the network to database as a single transaction
     * @param staff fetched data container
     */
    @Transaction
    suspend fun insertStaff(staff: DatabaseStaffContainer) {
        // Delete data from all tables
        deleteEmployees()
        deleteSpecialties()
        deleteEmployeesSpecialties()

        // Insert employees and specialties
        val employeesId = insertEmployees(staff.employees)
        insertSpecialties(staff.specialties)

        // Generate "employees_specialties" cross-reference table and insert to database
        // Не уверен, что включать логику в transaction функцию это правильное решение, однако, это позволяет
        // в рамках одной транзакции внести все изменения в БД (логика требуется для того, чтобы получить id
        // внесенных employees и сформировать таблицу соответствия "employees_specialties")
        val employeesToId = staff.employees.zip(employeesId).toMap()
        val employeesSpecialties: MutableList<DatabaseEmployeeSpecialty> = mutableListOf()
        staff.employeesToSpecialties.forEach { entry ->
            entry.value.forEach { specialty ->
                val employeeId = employeesToId[entry.key]
                val specialtyId = specialty.id
                if (employeeId != null)
                    employeesSpecialties.add(DatabaseEmployeeSpecialty(employeeId, specialtyId))
            }
        }
        insertEmployeesSpecialties(employeesSpecialties)
    }
}