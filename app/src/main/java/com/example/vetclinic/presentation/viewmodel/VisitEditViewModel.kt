package com.example.vetclinic.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetclinic.domain.model.Visit
import com.example.vetclinic.domain.repository.VetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VisitEditViewModel @Inject constructor(
    private val repository: VetRepository
) : ViewModel() {

    private val _patientId = MutableStateFlow<Long?>(null)
    private val _visitId = MutableStateFlow<Long?>(null)

    val date = MutableStateFlow(System.currentTimeMillis())
    val doctorName = MutableStateFlow("")
    val diagnosis = MutableStateFlow("")

    private val _saveEnabled = MutableStateFlow(false)
    val saveEnabled: StateFlow<Boolean> = _saveEnabled

    private val _savingState = MutableStateFlow(false)
    val savingState: StateFlow<Boolean> = _savingState

    init {
        viewModelScope.launch {
            combine(
                doctorName,
                diagnosis
            ) { doctorName, _ ->
                doctorName.isNotBlank()
            }.collect { enabled ->
                _saveEnabled.value = enabled
            }
        }
    }

    fun loadVisit(patientId: Long, visitId: Long?) {
        if (visitId == null || visitId == 0L) {
            _patientId.value = patientId
            return
        }

        viewModelScope.launch {
            _patientId.value = patientId
            _visitId.value = visitId
            val visit = repository.getVisitById(visitId)
            visit?.let {
                date.value = it.date
                doctorName.value = it.doctorName ?: ""
                diagnosis.value = it.diagnosis ?: ""
            }
        }
    }

    suspend fun saveVisit(): Boolean {
        return try {
            _savingState.value = true

            val patientId = _patientId.value
            if (patientId == null) {
                _savingState.value = false
                return false
            }

            val visit = Visit(
                id = _visitId.value ?: 0L,
                patientId = patientId,
                date = date.value,
                doctorName = doctorName.value.trim().takeIf { it.isNotBlank() },
                diagnosis = diagnosis.value.trim().takeIf { it.isNotBlank() }
            )

            if (visit.id == 0L) {
                repository.insertVisit(visit)
            } else {
                repository.updateVisit(visit)
            }

            _savingState.value = false
            true
        } catch (_: Exception) {
            _savingState.value = false
            false
        }
    }
}