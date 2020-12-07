package com.example.employees65apps.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import timber.log.Timber

/**
 * Class provides Room database building and singleton instantiation
 */
@TypeConverters(Converters::class)
@Database(
    entities = [
        DatabaseEmployee::class,
        DatabaseSpecialty::class,
        DatabaseEmployeeSpecialty::class
    ],
    version = 1,
    exportSchema = false
)
abstract class StaffDatabase : RoomDatabase() {
    abstract val staffDao: StaffDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: StaffDatabase? = null

        /**
         * If the database has been built provide its instance, otherwise build database and provide its instance
         */
        fun getInstance(context: Context): StaffDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        /**
         * Build StaffDatabase
         */
        private fun buildDatabase(context: Context): StaffDatabase {
            Timber.d("Build DB")

            return Room.databaseBuilder(
                context.applicationContext,
                StaffDatabase::class.java,
                "staff"
            ).build()
        }
    }
}