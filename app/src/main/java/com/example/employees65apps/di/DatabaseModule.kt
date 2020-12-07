package com.example.employees65apps.di

import android.content.Context
import com.example.employees65apps.database.StaffDao
import com.example.employees65apps.database.StaffDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideStaffDatabase(@ApplicationContext context: Context): StaffDatabase {
        return StaffDatabase.getInstance(context)
    }

    @Provides
    fun provideStaffDao(staffDatabase: StaffDatabase): StaffDao {
        return staffDatabase.staffDao
    }
}
