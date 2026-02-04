package com.example.vetclinic.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patients")
data class PatientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val species: String,
    val breed: String? = null,
    val age: Int? = null,
    val weight: Double? = null,
    val createdAt: Long = System.currentTimeMillis()
)
