package com.example.vetclinic.domain.repository

import com.example.vetclinic.domain.model.Patient
import com.example.vetclinic.domain.model.Visit
import kotlinx.coroutines.flow.Flow

interface VetRepository {
    fun getAllPatients(): Flow<List<Patient>>
    suspend fun getPatientById(id: Long): Patient?
    suspend fun insertPatient(patient: Patient): Long
    suspend fun updatePatient(patient: Patient)
    suspend fun deletePatient(patient: Patient)

    fun getVisitsByPatient(patientId: Long): Flow<List<Visit>>
    suspend fun getVisitById(id: Long): Visit?
    suspend fun insertVisit(visit: Visit): Long
    suspend fun updateVisit(visit: Visit)
    suspend fun deleteVisit(visit: Visit)
}