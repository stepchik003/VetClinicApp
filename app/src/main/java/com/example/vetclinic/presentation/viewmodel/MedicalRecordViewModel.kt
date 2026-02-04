package com.example.vetclinic.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetclinic.domain.model.Patient
import com.example.vetclinic.domain.model.UiState
import com.example.vetclinic.domain.model.Visit
import com.example.vetclinic.domain.repository.VetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicalRecordViewModel @Inject constructor(
    private val repository: VetRepository
) : ViewModel() {

    private val _selectedPatientId = MutableStateFlow<Long?>(null)

    private val _patientState = MutableStateFlow<UiState<Patient>>(UiState.Loading)
    val patientState: StateFlow<UiState<Patient>> = _patientState.asStateFlow()

    private val _visitsState = MutableStateFlow<UiState<List<Visit>>>(UiState.Loading)
    val visitsState: StateFlow<UiState<List<Visit>>> = _visitsState.asStateFlow()

    fun setPatientId(patientId: Long) {
        if (_selectedPatientId.value != patientId) {
            _selectedPatientId.value = patientId
            loadPatient(patientId)
            loadVisits(patientId)
        }
    }

    private fun loadPatient(patientId: Long) {
        viewModelScope.launch {
            _patientState.value = UiState.Loading
            try {
                val patient = repository.getPatientById(patientId)
                if (patient != null) {
                    _patientState.value = UiState.Success(patient)
                } else {
                    _patientState.value = UiState.Error("Пациент не найден")
                }
            } catch (e: Exception) {
                _patientState.value = UiState.Error(e.message ?: "Ошибка при загрузке пациентов")
            }
        }
    }

    private fun loadVisits(patientId: Long) {
        viewModelScope.launch {
            repository.getVisitsByPatient(patientId)
                .catch { e ->
                    _visitsState.value = UiState.Error(e.message ?: "Ошибка при загрузке записей")
                }
                .collect { visits ->
                    _visitsState.value = UiState.Success(visits.sortedByDescending { it.date })
                }
        }
    }

    fun deleteVisit(visit: Visit) {
        viewModelScope.launch {
            try {
                repository.deleteVisit(visit)
            } catch (_: Exception) {
            }
        }
    }
}