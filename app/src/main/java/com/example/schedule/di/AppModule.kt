package com.example.schedule.di

import android.content.Context
import com.example.schedule.data.local.ScheduleDao
import com.example.schedule.data.local.ScheduleDatabase
import com.example.schedule.repository.ScheduleItemsRepository
import com.example.schedule.util.ScheduleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.* //ktlint-disable
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun proivdeShopDatabaseInstance(
        @ApplicationContext context: Context
    ) = ScheduleDatabase(context)

    @Provides
    @Singleton
    fun provideCalendarInstance() = Calendar.getInstance()

    @Provides
    @Singleton
    fun provideScheduleRepository(db: ScheduleDatabase, calendar: Calendar) = ScheduleItemsRepository(db, calendar) as ScheduleRepository
}
