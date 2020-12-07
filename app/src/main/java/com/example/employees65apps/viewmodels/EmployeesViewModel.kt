package com.example.employees65apps.viewmodels

import androidx.lifecycle.*
import com.example.employees65apps.domain.EmployeeWithSpecialties
import com.example.employees65apps.domain.Specialty
import com.example.employees65apps.repository.StaffRepository
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch

class EmployeesViewModel @AssistedInject constructor(
    private val staffRepository: StaffRepository,
    @Assisted val specialty: Specialty
) : ViewModel() {

    val employees = MutableLiveData<List<EmployeeWithSpecialties>>()

    private val _navigateToSelectedEmployee = MutableLiveData<EmployeeWithSpecialties>()
    val navigateToSelectedEmployee: LiveData<EmployeeWithSpecialties>
        get() = _navigateToSelectedEmployee

    init {
        viewModelScope.launch {
            employees.value = staffRepository.getEmployeesBySpecialty(specialty.id)
        }
    }

    @AssistedInject.Factory
    interface AssistedFactory {
        fun create(specialty: Specialty): EmployeesViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            specialty: Specialty
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(specialty) as T
            }
        }
    }

    fun onNavigateToSelectedEmployee(employee: EmployeeWithSpecialties) {
        _navigateToSelectedEmployee.value = employee
    }

    fun navigateToSelectedEmployeeComplete() {
        _navigateToSelectedEmployee.value = null
    }
}