package com.cornershop.counterstest.welcome.di

import com.cornershop.counterstest.welcome.data.WelcomeInfrastructure
import com.cornershop.counterstest.welcome.data.WelcomeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object WelcomeModule {

    @Provides
    fun provideWelcomeService(infrastructure: WelcomeInfrastructure): WelcomeService =
        infrastructure
}
