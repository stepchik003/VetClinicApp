package com.example.vetclinic.data.repository

import com.example.vetclinic.data.database.dao.VetDao
import com.example.vetclinic.data.mapper.PatientMapper
import com.example.vetclinic.data.mapper.VisitMapper
import com.example.vetclinic.domain.model.Patient
import com.example.vetclinic.domain.model.Visit
import com.example.vetclinic.domain.repository.VetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VetRepositoryImpl @Inject constructor(
    private val dao: VetDao
) : VetRepository {
    override fun getAllPatients(): Flow<List<Patient>> {
        return dao.getAllPatients().map { entities -> PatientMapper.toDomainList(entities) }
    }

    override suspend fun getPatientById(id: Long): Patient? {
        return dao.getPatientById(id = id)?.let { PatientMapper.toDomain(it) }
    }

    override suspend fun insertPatient(patient: Patient): Long {
        return dao.insertPatient(PatientMapper.toEntity(patient))
    }

    override suspend fun updatePatient(patient: Patient) {
        dao.updatePatient(PatientMapper.toEntity(patient))
    }

    override suspend fun deletePatient(patient: Patient) {
        dao.deletePatient(PatientMapper.toEntity(patient))
    }

    override fun getVisitsByPatient(patientId: Long): Flow<List<Visit>> {
        return dao.getVisitsForPatient(patientId)
            .map { entities -> VisitMapper.toDomainList(entities) }
    }

    override suspend fun getVisitById(id: Long): Visit? {
        return dao.getVisitById(id = id)?.let { VisitMapper.toDomain(it) }
    }

    override suspend fun insertVisit(visit: Visit): Long {
        return dao.insertVisit(VisitMapper.toEntity(visit))
    }

    override suspend fun updateVisit(visit: Visit) {
        dao.updateVisit(VisitMapper.toEntity(visit))
    }

    override suspend fun deleteVisit(visit: Visit) {
        dao.deleteVisit(VisitMapper.toEntity(visit))
    }
}