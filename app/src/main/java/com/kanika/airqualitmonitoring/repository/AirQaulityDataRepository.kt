package com.kanika.airqualitmonitoring.repository

import com.kanika.airqualitmonitoring.data.datasource.LocalSource
import com.kanika.airqualitmonitoring.data.datasource.RemoteSource
import com.kanika.airqualitmonitoring.data.dao.AirQaulityDataDao
import com.kanika.airqualitmonitoring.listener.FailConnectionListener
import com.kanika.airqualitmonitoring.listener.AirQaulityDataSocketResponseListener
import com.kanika.airqualitmonitoring.model.AirQaulityDataModel
import javax.inject.Inject

class AirQaulityDataRepository @Inject constructor(
    private val cityAqiDao: AirQaulityDataDao,
    private val airQaulityDataSocketImpl: AirQaulityDataSocketImpl
) : RemoteSource, LocalSource, AirQaulityDataSocketResponseListener {

    init {
        airQaulityDataSocketImpl.setResponseListener(this)
    }

    var failConnectionListener: FailConnectionListener? = null

    override fun connect() {
        airQaulityDataSocketImpl.connect()
    }

    override fun cancel() {
        airQaulityDataSocketImpl.cancel()
    }

    override fun insertData(dataList: List<AirQaulityDataModel>) {
        cityAqiDao.insertAll(dataList)
    }

    override fun getLastValuesOf(city: String) = cityAqiDao.getLastValuesOf(city)
    override fun getLatestData() = cityAqiDao.getLastValues()
    override fun onSuccess(list: List<AirQaulityDataModel>) {
        insertData(list.onEach {
            it.seconds = System.currentTimeMillis() / 1000
        })
    }

    override fun onFailure() {
        failConnectionListener?.onSocketFailure()
    }
}