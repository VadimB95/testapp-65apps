package com.example.employees65apps.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.employees65apps.domain.Specialty
import com.example.employees65apps.repository.StaffRepository
import kotlinx.coroutines.launch
import java.io.IOException

enum class StaffApiStatus { LOADING, ERROR, DONE }

class SpecialtiesViewModel @ViewModelInject constructor(
    private val repository: StaffRepository
) : ViewModel() {
    val specialties = repository.observeSpecialties()

    // LiveData that stores the status of the most recent refresh request
    private val _refreshStatus = MutableLiveData<StaffApiStatus>()
    val refreshStatus: LiveData<StaffApiStatus>
        get() = _refreshStatus

    private val _navigateToSelectedSpecialty = MutableLiveData<Specialty>()
    val navigateToSelectedSpecialty: LiveData<Specialty>
        get() = _navigateToSelectedSpecialty

    init {
        refreshDataFromRepository()
    }

    /**
     * Refresh data from the repository. Use a coroutine launch to run in a
     * background thread.
     */
    fun refreshDataFromRepository() {
        _refreshStatus.value = StaffApiStatus.LOADING
        viewModelScope.launch {
            try {
                repository.refreshStaff()
                _refreshStatus.value = StaffApiStatus.DONE
            } catch (networkError: IOException) {
                // Show a Toast error message and hide refresh circle
                _refreshStatus.value = StaffApiStatus.ERROR
            }
        }
    }

    fun onNavigateToSelectedSpecialty(specialty: Specialty) {
        _navigateToSelectedSpecialty.value = specialty
    }

    fun navigateToSelectedSpecialtyComplete() {
        _navigateToSelectedSpecialty.value = null
    }
}