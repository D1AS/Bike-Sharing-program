package com.example.bikeshare.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikeshare.data.Bicycle
import com.example.bikeshare.repositories.BicycleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // Hilt ViewModel annotation for dependency injection.
class BicycleViewModel @Inject constructor(
    private val repository: BicycleRepository // Injecting the repository.
) : ViewModel() {

    // Exposing bicycles as StateFlow to observe changes in the UI.
    val bicycles: StateFlow<List<Bicycle>> = repository.getBicycles()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // New StateFlow for not found bicycles
    val bicyclesNotFound: StateFlow<List<Bicycle>> = repository.getBicyclesNotFound()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val filteredBicycles: StateFlow<List<Bicycle>> = repository.getBicycles()
        .map { bicycles -> bicycles.filter { !it.isFound } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun updateBicycle(bicycle: Bicycle) {
        viewModelScope.launch {
            repository.updateBicycle(bicycle)
        }
    }

    fun insertBicycle(bicycle: Bicycle) {
        viewModelScope.launch {
            repository.insertBicycle(bicycle)
        }
    }

    init {
        // Populate database if empty when ViewModel is created.
        viewModelScope.launch {
            repository.populateDatabaseIfEmpty()
        }
    }

    fun clearDatabase() {
        viewModelScope.launch {
            repository.clearDatabase()
        }
    }

}
