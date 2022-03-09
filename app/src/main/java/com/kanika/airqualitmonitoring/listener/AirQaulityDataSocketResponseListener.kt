package com.kanika.airqualitmonitoring.listener

import com.kanika.airqualitmonitoring.model.AirQaulityDataModel

interface AirQaulityDataSocketResponseListener {
    fun onSuccess(list: List<AirQaulityDataModel>)
    fun onFailure()
}