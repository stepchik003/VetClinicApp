package com.example.vetclinic.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetclinic.domain.model.Patient
import com.example.vetclinic.domain.model.UiState
import com.example.vetclinic.domain.repository.VetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientListViewModel @Inject constructor(
    private val repository: VetRepository
) : ViewModel() {

    private val _patientsState = MutableStateFlow<UiState<List<Patient>>>(UiState.Loading)
    val patientsState: StateFlow<UiState<List<Patient>>> = _patientsState.asStateFlow()

    init {
        loadPatients()
    }

    private fun loadPatients() {
        viewModelScope.launch {
            repository.getAllPatients()
                .catch { e ->
                    _patientsState.value = UiState.Error(e.message ?: "Ошибка при загрузке пациентов")
                }
                .collect { patients ->
                    _patientsState.value = UiState.Success(patients)
                }
        }
    }

    fun deletePatient(patient: Patient) {
        viewModelScope.launch {
            repository.deletePatient(patient)
        }
    }
}