package com.kanika.airqualitmonitoring.data.datasource

interface RemoteSource {
    fun connect()
    fun cancel()
}