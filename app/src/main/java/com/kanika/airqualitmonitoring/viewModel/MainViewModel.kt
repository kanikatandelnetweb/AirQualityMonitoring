package com.kanika.airqualitmonitoring.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kanika.airqualitmonitoring.model.AirQaulityDataModel
import com.kanika.airqualitmonitoring.repository.AirQaulityDataRepository
import com.kanika.airqualitmonitoring.listener.FailConnectionListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val airQaulityDataRepository: AirQaulityDataRepository) : ViewModel() {

    var selectedCityLiveData = MutableLiveData<String>()

    fun getLatestData(): Flow<List<AirQaulityDataModel>> = airQaulityDataRepository.getLatestData()

    fun getSelectedCityData(): Flow<List<AirQaulityDataModel>>? {
        val city = selectedCityLiveData.value ?: return null
        return airQaulityDataRepository.getLastValuesOf(city)
    }

    fun connect() {
        airQaulityDataRepository.connect()
    }

    fun cancel() {
        airQaulityDataRepository.cancel()
    }

    fun setConnectionFailureListener(failConnectionListener: FailConnectionListener?){
        airQaulityDataRepository.failConnectionListener = failConnectionListener
    }
}