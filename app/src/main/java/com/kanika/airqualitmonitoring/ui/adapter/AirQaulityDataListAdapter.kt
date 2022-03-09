package com.kanika.airqualitmonitoring.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kanika.airqualitmonitoring.*
import com.kanika.airqualitmonitoring.databinding.AirQualityItemBinding
import com.kanika.airqualitmonitoring.databinding.AirQualityItemBinding.inflate
import com.kanika.airqualitmonitoring.model.AirQaulityDataModel

class AirQaulityDataListAdapter(private val onCityClicked: (AirQaulityDataModel) -> Unit) :
    RecyclerView.Adapter<AirQaulityDataListAdapter.AqiListViewHolder>() {

    private val cityAqiList: ArrayList<AirQaulityDataModel> = arrayListOf()

    fun updateList(dataList: List<AirQaulityDataModel>) {
        mergeNewAndOldList(dataList, cityAqiList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AqiListViewHolder {
        val itemBinding = inflate(LayoutInflater.from(parent.context), parent, false)
        return AqiListViewHolder(itemBinding, onCityClicked)
    }

    override fun onBindViewHolder(holder: AqiListViewHolder, position: Int) {
        val aqiBean = cityAqiList[position]
        holder.bind(aqiBean)
    }

    override fun getItemCount() = cityAqiList.size

    inner class AqiListViewHolder(
        private val itemBinding: AirQualityItemBinding,
        private val onCityClicked: (AirQaulityDataModel) -> Unit
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(airQaulityDataModel: AirQaulityDataModel) {
            itemBinding.txtCity.text = airQaulityDataModel.city
            itemBinding.txtAqi.text = airQaulityDataModel.aqi.roundTo(2)
           // setCategoryColor(airQaulityDataModel.aqi,itemBinding.txtCategory)
            itemBinding.txtCategory.text = getAqiStatus(airQaulityDataModel.aqi)
            itemBinding.txtAqiStatus.text = airQaulityDataModel.lastUpdated
            val aqiColor = airQaulityDataModel.aqiColor
            if (aqiColor != null)
                itemBinding.cvAqi.setBackgroundColor(
                    ContextCompat.getColor(
                        itemBinding.root.context,
                        aqiColor
                    )
                )
            itemView.setOnClickListener { onCityClicked(airQaulityDataModel) }
        }
    }


}

