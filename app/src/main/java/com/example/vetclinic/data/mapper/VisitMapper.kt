package com.example.vetclinic.data.mapper

import com.example.vetclinic.data.database.entity.VisitEntity
import com.example.vetclinic.domain.model.Visit

object VisitMapper {
    fun toDomain(entity: VisitEntity): Visit {
        return Visit(
            id = entity.id,
            patientId = entity.patientId,
            date = entity.date,
            doctorName = entity.doctorName,
            diagnosis = entity.diagnosis,
            createdAt = entity.createdAt
        )
    }

    fun toEntity(domain: Visit): VisitEntity {
        return VisitEntity(
            id = domain.id,
            patientId = domain.patientId,
            date = domain.date,
            doctorName = domain.doctorName,
            diagnosis = domain.diagnosis,
            createdAt = domain.createdAt
        )
    }

    fun toDomainList(entities: List<VisitEntity>): List<Visit> {
        return entities.map { toDomain(it) }
    }

    fun toEntityList(domains: List<Visit>): List<VisitEntity> {
        return domains.map { toEntity(it) }
    }
}