package com.kanika.airqualitmonitoring.repository

import android.util.Log
import com.kanika.airqualitmonitoring.model.AirQaulityDataModel
import com.google.gson.Gson
import com.kanika.airqualitmonitoring.listener.AirQaulityDataSocketResponseListener
import okhttp3.*
import java.lang.reflect.Type
import javax.inject.Inject

class AirQaulityDataSocketImpl @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val request: Request,
    private val gson: Gson,
    private val type: Type
) : WebSocketListener() {

    companion object {
        const val TAG = "WebSocketImpl"
    }

    private var webSocket: WebSocket? = null
    private var airQaulityDataSocketResponseListener: AirQaulityDataSocketResponseListener? = null

    fun setResponseListener(airQaulityDataSocketResponseListener: AirQaulityDataSocketResponseListener) {
        this.airQaulityDataSocketResponseListener = airQaulityDataSocketResponseListener
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Log.d(TAG, "onMessage() : text : $text")
        val list = gson.fromJson<List<AirQaulityDataModel>>(text, type)
        airQaulityDataSocketResponseListener?.onSuccess(list)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        airQaulityDataSocketResponseListener?.onFailure()
    }

    fun connect() {
        webSocket = okHttpClient.newWebSocket(request, this)
    }

    fun cancel() {
        webSocket?.cancel()
    }
}