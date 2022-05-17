package com.pti.sheldons_schedule.di

import android.content.Context
import com.pti.sheldons_schedule.db.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideDao(appDatabased: AppDatabase): EventDao = appDatabased.eventDao()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context) = createDatabase(appContext)

    @Provides
    @Singleton
    fun provideRepository(eventDao: EventDao): EventRepository = EventRepositoryImpl(eventDao)
}