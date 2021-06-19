package com.cornershop.counterstest.counter.di

import com.cornershop.counterstest.counter.data.CounterInfrastructure
import com.cornershop.counterstest.counter.data.CounterService
import com.cornershop.counterstest.counter.data.CountersGateway
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(ViewModelComponent::class)
object CountersModule {

    @Provides
    fun provideCountersGateway(retrofit: Retrofit): CountersGateway = retrofit.create()

    @Provides
    fun provideCounterService(infrastructure: CounterInfrastructure): CounterService = infrastructure
}
