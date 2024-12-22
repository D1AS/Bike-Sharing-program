package com.example.bikeshare.di

import android.content.Context
import com.example.bikeshare.data.AppDatabase
import com.example.bikeshare.data.BicycleDao
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
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideBicycleDao(database: AppDatabase): BicycleDao {
        return database.bicycleDao()
    }

}
