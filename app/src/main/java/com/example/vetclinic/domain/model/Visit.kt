package com.example.vetclinic.domain.model

data class Visit(
    val id: Long = 0,
    val patientId: Long,
    val date: Long,
    val doctorName: String? = null,
    val diagnosis: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
