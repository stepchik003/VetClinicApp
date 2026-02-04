package com.example.vetclinic.di

import android.content.Context
import androidx.room.Room
import com.example.vetclinic.data.database.VetDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): VetDatabase {
        return Room.databaseBuilder(
            context,
            VetDatabase::class.java,
            "vetclinic.db"
        ).fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    fun provideDao(database: VetDatabase) = database.dao()
}