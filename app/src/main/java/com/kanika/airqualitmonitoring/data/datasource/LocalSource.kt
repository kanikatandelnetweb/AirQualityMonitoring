package com.kanika.airqualitmonitoring.data.datasource

import com.kanika.airqualitmonitoring.model.AirQaulityDataModel
import kotlinx.coroutines.flow.Flow

interface LocalSource {
    fun insertData(dataList: List<AirQaulityDataModel>)
    fun getLatestData(): Flow<List<AirQaulityDataModel>>
    fun getLastValuesOf(city: String): Flow<List<AirQaulityDataModel>>
}