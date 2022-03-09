package com.kanika.airqualitmonitoring.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.kanika.airqualitmonitoring.SOCKET_DELAY
import com.kanika.airqualitmonitoring.calculateTimeDifference
import com.kanika.airqualitmonitoring.model.AirQaulityDataModel
import com.kanika.airqualitmonitoring.roundTo
import com.kanika.airqualitmonitoring.setAqiColor
import com.kanika.airqualitmonitoring.viewModel.MainViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.kanika.airqualitmonitoring.databinding.FragmentGraphBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GraphFragment : Fragment() {

    private var mBinding: FragmentGraphBinding? = null
    private var mBarData: BarData? = null
    private val mainViewModel: MainViewModel by activityViewModels()
    private val city = "City"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentGraphBinding.inflate(inflater, container, false)
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding?.barChart?.xAxis?.position = XAxis.XAxisPosition.BOTTOM
        mBinding?.barChart?.description?.isEnabled = false
        mBinding?.barChart?.legend?.isEnabled = false
        lifecycleScope.launch {
            mainViewModel.getSelectedCityData()?.collect {
                setBarChartData(it)
                delay(SOCKET_DELAY)
            }
        }
        mainViewModel.selectedCityLiveData.observe(viewLifecycleOwner){
            mBinding?.txtCityName?.text = it
        }
    }

    private fun setBarChartData(list: List<AirQaulityDataModel>) {
        val entryList: ArrayList<BarEntry> = ArrayList()
        ArrayList(list).sortBy { it.seconds }
        val colorList = arrayListOf<Int>()
        list.forEach {
            colorList.add(ContextCompat.getColor(requireContext(), setAqiColor(it.aqi)))
            val seconds = it.seconds
            if (seconds != null) {
                entryList.add(
                    BarEntry(
                        calculateTimeDifference(seconds).toFloat() ,
                        it.aqi.roundTo(2).toFloat()
                    )
                )
            }
        }
        val barDataSet = BarDataSet(entryList, city)
        barDataSet.colors = colorList
        mBarData = BarData(barDataSet)
        mBinding?.barChart?.data = mBarData
        mBinding?.barChart?.setVisibleXRangeMaximum(10f)
        mBinding?.barChart?.invalidate()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}