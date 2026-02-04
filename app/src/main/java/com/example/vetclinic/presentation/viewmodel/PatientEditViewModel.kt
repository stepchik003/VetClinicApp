package com.example.vetclinic.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetclinic.domain.model.Patient
import com.example.vetclinic.domain.repository.VetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientEditViewModel @Inject constructor(
    private val repository: VetRepository
) : ViewModel() {

    private val _patientId = MutableStateFlow<Long?>(null)

    val name = MutableStateFlow("")
    val species = MutableStateFlow("")
    val breed = MutableStateFlow("")
    val age = MutableStateFlow("")
    val weight = MutableStateFlow("")

    private val _saveEnabled = MutableStateFlow(false)
    val saveEnabled: StateFlow<Boolean> = _saveEnabled

    private val _savingState = MutableStateFlow(false)
    val savingState: StateFlow<Boolean> = _savingState

    init {
        viewModelScope.launch {
            combine(
                name,
                species,
                age,
                weight
            ) { name, species, age, weight ->
                name.isNotBlank() && species.isNotBlank() &&
                        (age.isBlank() || age.toIntOrNull() != null) &&
                        (weight.isBlank() || weight.toDoubleOrNull() != null)
            }.collect { enabled ->
                _saveEnabled.value = enabled
            }
        }
    }

    fun loadPatient(patientId: Long?) {
        if (patientId == null || patientId == 0L) return

        viewModelScope.launch {
            _patientId.value = patientId
            val patient = repository.getPatientById(patientId)
            patient?.let {
                name.value = it.name
                species.value = it.species
                breed.value = it.breed ?: ""
                age.value = it.age?.toString() ?: ""
                weight.value = it.weight?.toString() ?: ""
            }
        }
    }

    suspend fun savePatient(): Boolean {
        return try {
            _savingState.value = true

            val ageInt = age.value.toIntOrNull()
            val weightDouble = weight.value.toDoubleOrNull()

            val patient = Patient(
                id = _patientId.value ?: 0L,
                name = name.value.trim(),
                species = species.value.trim(),
                breed = breed.value.trim().takeIf { it.isNotBlank() },
                age = ageInt,
                weight = weightDouble
            )

            if (patient.id == 0L) {
                repository.insertPatient(patient)
            } else {
                repository.updatePatient(patient)
            }

            _savingState.value = false
            true
        } catch (_: Exception) {
            _savingState.value = false
            false
        }
    }
}