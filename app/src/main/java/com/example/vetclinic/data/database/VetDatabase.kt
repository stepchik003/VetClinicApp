package com.example.vetclinic.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vetclinic.data.database.dao.VetDao
import com.example.vetclinic.data.database.entity.PatientEntity
import com.example.vetclinic.data.database.entity.VisitEntity

@Database(
    entities = [PatientEntity::class, VisitEntity::class],
    version = 1,
    exportSchema = false
)
abstract class VetDatabase : RoomDatabase() {
    abstract fun dao(): VetDao
}