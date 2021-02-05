package com.example.schedule.di

import android.content.Context
import android.location.Geocoder
import com.example.schedule.data.local.ScheduleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun proivdeShopDatabaseInstance(
        @ApplicationContext context: Context
    ) = ScheduleDatabase(context)

    @Provides
    @Singleton
    fun provideGeocoder(
            @ApplicationContext context: Context
    ) = Geocoder(context)
}
