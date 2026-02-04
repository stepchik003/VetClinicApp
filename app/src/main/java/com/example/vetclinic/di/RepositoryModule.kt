package com.example.vetclinic.di

import com.example.vetclinic.data.repository.VetRepositoryImpl
import com.example.vetclinic.domain.repository.VetRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindVetRepository(
        repositoryImpl: VetRepositoryImpl
    ): VetRepository
}