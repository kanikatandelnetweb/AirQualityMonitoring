package com.kanika.airqualitmonitoring.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kanika.airqualitmonitoring.model.AirQaulityDataModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AirQaulityDataDao {
    @Insert
    fun insertAll(dataList: List<AirQaulityDataModel>)

    @Query("DELETE FROM AirQaulityDataModel")
    fun deleteData()

    @Query("SELECT * FROM AirQaulityDataModel ca WHERE ca.seconds = (SELECT MAX(seconds) FROM AirQaulityDataModel a WHERE a.city = ca.city) GROUP BY ca.city")
    fun getLastValues(): Flow<List<AirQaulityDataModel>>

    @Query("SELECT * FROM AirQaulityDataModel baq WHERE baq.city = :city ORDER BY baq.seconds DESC limit 50")
    fun getLastValuesOf(city: String): Flow<List<AirQaulityDataModel>>
}