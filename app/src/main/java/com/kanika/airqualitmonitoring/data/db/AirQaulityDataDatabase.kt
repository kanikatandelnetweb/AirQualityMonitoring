package com.kanika.airqualitmonitoring.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kanika.airqualitmonitoring.data.dao.AirQaulityDataDao
import com.kanika.airqualitmonitoring.model.AirQaulityDataModel

/**
 * database which is used to store the incoming data
 */
@Database(
    entities = [AirQaulityDataModel::class],
    version = 1,
    exportSchema = false
)
abstract class AirQaulityDataDatabase : RoomDatabase() {
    abstract fun cityAqiDao(): AirQaulityDataDao

    companion object {
        @Volatile
        private var airQaulityDataDbInstance: AirQaulityDataDatabase? = null

        fun createDb(context: Context): AirQaulityDataDatabase =
            airQaulityDataDbInstance ?: synchronized(this) {
                airQaulityDataDbInstance ?: buildDb(context).also {
                    airQaulityDataDbInstance = it
                }
            }

        private fun buildDb(context: Context) =
            Room.databaseBuilder(context, AirQaulityDataDatabase::class.java, "CityAqi.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}