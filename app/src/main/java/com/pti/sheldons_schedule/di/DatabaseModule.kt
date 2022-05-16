package com.pti.sheldons_schedule.di

import android.content.Context
import androidx.room.Room
import com.pti.sheldons_schedule.db.AppDatabase
import com.pti.sheldons_schedule.db.EventDao
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
    fun provideAppDatabase(@ApplicationContext appContext: Context) = Room.databaseBuilder(
        appContext,
        AppDatabase::class.java,
        "EventDatabase"
    ).build()
}