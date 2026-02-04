package com.example.vetclinic.data.mapper

import com.example.vetclinic.data.database.entity.PatientEntity
import com.example.vetclinic.domain.model.Patient

object PatientMapper {
    fun toDomain(entity: PatientEntity): Patient {
        return Patient(
            id = entity.id,
            name = entity.name,
            species = entity.species,
            breed = entity.breed,
            age = entity.age,
            weight = entity.weight,
            createdAt = entity.createdAt
        )
    }

    fun toEntity(domain: Patient): PatientEntity {
        return PatientEntity(
            id = domain.id,
            name = domain.name,
            species = domain.species,
            breed = domain.breed,
            age = domain.age,
            weight = domain.weight,
            createdAt = domain.createdAt
        )
    }

    fun toDomainList(entities: List<PatientEntity>): List<Patient> {
        return entities.map { toDomain(it) }
    }

    fun toEntityList(domains: List<Patient>): List<PatientEntity> {
        return domains.map { toEntity(it) }
    }
}