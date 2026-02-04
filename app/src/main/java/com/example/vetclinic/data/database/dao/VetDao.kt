package com.example.vetclinic.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.vetclinic.data.database.entity.PatientEntity
import com.example.vetclinic.data.database.entity.VisitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VetDao {
    @Query("SELECT * FROM patients ORDER BY name")
    fun getAllPatients(): Flow<List<PatientEntity>>

    @Query("SELECT * FROM patients WHERE id = :id")
    suspend fun getPatientById(id: Long): PatientEntity?

    @Insert
    suspend fun insertPatient(patient: PatientEntity): Long

    @Update
    suspend fun updatePatient(patient: PatientEntity)

    @Delete
    suspend fun deletePatient(patient: PatientEntity)

    @Query("SELECT * FROM visits WHERE patientId = :patientId ORDER BY date DESC")
    fun getVisitsForPatient(patientId: Long): Flow<List<VisitEntity>>

    @Query("SELECT * FROM visits WHERE id = :id")
    suspend fun getVisitById(id: Long): VisitEntity?

    @Insert
    suspend fun insertVisit(visit: VisitEntity): Long

    @Update
    suspend fun updateVisit(visit: VisitEntity)

    @Delete
    suspend fun deleteVisit(visit: VisitEntity)

    @Query("DELETE FROM visits where patientId = :patientId")
    suspend fun deleteVisitsForPatient(patientId: Long)
}