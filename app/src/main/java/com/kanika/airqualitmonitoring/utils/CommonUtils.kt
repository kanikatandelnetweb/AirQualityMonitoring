package com.kanika.airqualitmonitoring

import android.content.Context
import android.graphics.Color
import com.kanika.airqualitmonitoring.model.AirQaulityDataModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor
import android.net.NetworkInfo
import android.net.ConnectivityManager
import android.text.SpannableStringBuilder
import android.widget.TextView
import androidx.core.text.color

val SIMPLE_DATE_FORMAT = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH)

/**
 * @param dataList is the new city data list
 * @param adapterDataList is the current adapter data from RecyclerView
 *
 * This method adds color and time to the existing list
 */
fun mergeNewAndOldList(
    dataList: List<AirQaulityDataModel>,
    adapterDataList: MutableList<AirQaulityDataModel>
): MutableList<AirQaulityDataModel> {
    addColorToDataList(dataList, adapterDataList)
    addTimeToNewAdapterList(adapterDataList)

    return adapterDataList
}

/**
 * This method updates the time for adapter items
 * @param adapterDataList is the current adapter list from recyclerView
 */
private fun addTimeToNewAdapterList(adapterDataList: MutableList<AirQaulityDataModel>) {
    adapterDataList.forEach {
        it.seconds?.let { seconds ->
            val diffSeconds = calculateTimeDifference(seconds)
            when {
                diffSeconds < 60 -> {
                    it.lastUpdated = "$diffSeconds $SECONDS_AGO"
                }
                diffSeconds < 60 * 60 -> {
                    val minutes = floor(diffSeconds / 60.0)
                    it.lastUpdated = "$minutes $MINUTES_AGO"
                }
                diffSeconds < 60 * 60 * 60 -> {
                    val hours = floor(diffSeconds / 60.0 * 60)
                    it.lastUpdated = "$hours $HOURS_AGO"
                }
                diffSeconds > 60 * 60 * 60 -> {
                    it.lastUpdated = SIMPLE_DATE_FORMAT.format(diffSeconds * 1000)
                }
            }
        }

    }
}

/**
 * This methods adds color for the aqi
 */
private fun addColorToDataList(
    dataList: List<AirQaulityDataModel>,
    adapterDataList: MutableList<AirQaulityDataModel>
) {
    dataList.forEach {
        val itemIndex = adapterDataList.indexOf(it)
        it.aqiColor = setAqiColor(it.aqi)
        //-1 is returned when items are not matched
        if (itemIndex == -1) {
            adapterDataList.add(it)
        } else {
            adapterDataList[itemIndex] = it
        }
    }
}

/**
 * @param seconds is the current city object updated time in seconds
 * This methods returns the time difference current System time
 */
fun calculateTimeDifference(seconds: Long) = System.currentTimeMillis() / 1000 - seconds

fun setAqiColor(aqi: Double): Int {
    if (aqi > 0 && aqi < 50) {
        return R.color.aqi_good //Good
    } else if (aqi > 50 && aqi < 100) {
        return R.color.aqi_satisfactory //Moderate
    } else if (aqi > 100 && aqi < 200) {
        return R.color.aqi_moderate //Unhealthy for Sensitive Groups
    } else if (aqi > 200 && aqi < 300) {
        return R.color.aqi_poor //Unhealthy
    } else if (aqi > 300 && aqi < 400) {
        return R.color.aqi_verypoor //Very Unhealthy
    } else if (aqi > 400 && aqi < 500) {
        return R.color.aqi_severe //Hazardous
    }
    return R.color.black
}

 fun setCategoryColor(aqi: Double, txtCategory: TextView) {
    if (aqi > 0 && aqi < 50) {
        val codeColor = Color.rgb(85,168,79)
        txtCategory.text= setColor(codeColor,"Good")
    } else if (aqi >50 && aqi < 100) {
        val codeColor = Color.rgb(163,200,83)
        txtCategory.text= setColor(codeColor,"Satisfactory")
    } else if (aqi > 100 && aqi < 200) {
        val codeColor = Color.rgb(255,248,51)
        txtCategory.text= setColor(codeColor,"Moderate")
    } else if (aqi > 200 && aqi < 300) {
        val codeColor = Color.rgb(242,156,51)
        txtCategory.text= setColor(codeColor,"Poor")
    } else if (aqi > 300 && aqi < 400) {
        val codeColor = Color.rgb(233,63,51)
        txtCategory.text=  setColor(codeColor,"Very Poor")
    } else if (aqi > 400 && aqi < 500) {
        val codeColor = Color.rgb(175,45,36)
        txtCategory.text= setColor(codeColor,"Severe")
    }
}
fun setColor(codeColor: Int, categoryName: String,) : SpannableStringBuilder {
    val commonColor = Color.rgb(77,77,77)
    val text = SpannableStringBuilder()
        .color(commonColor) { append("Air Quality is ") }
        .color(codeColor) {append(categoryName)}
    return text
}

fun Number.roundTo(
    numFractionDigits: Int
) = "%.${numFractionDigits}f".format(this, Locale.ENGLISH)

fun isNetworkAvailable(context: Context?): Boolean {
    val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}