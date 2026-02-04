package com.example.vetclinic.domain.model

data class Patient(
    val id: Long = 0,
    val name: String,
    val species: String,
    val breed: String? = null,
    val age: Int? = null,
    val weight: Double? = null,
    val createdAt: Long = System.currentTimeMillis()
)
