package com.kanika.airqualitmonitoring.ui.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.kanika.airqualitmonitoring.R
import com.kanika.airqualitmonitoring.databinding.FragmentHomeBinding
import com.kanika.airqualitmonitoring.isNetworkAvailable
import com.kanika.airqualitmonitoring.model.AirQaulityDataModel
import com.kanika.airqualitmonitoring.listener.FailConnectionListener
import com.kanika.airqualitmonitoring.ui.adapter.AirQaulityDataListAdapter
import com.kanika.airqualitmonitoring.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), FailConnectionListener {

    private var mBinding: FragmentHomeBinding? = null
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var mConnectivityManager: ConnectivityManager
    private val mAqiListAdapter = AirQaulityDataListAdapter { onCityClicked(it) }
    private val networkRequest = NetworkRequest.Builder().build()

    private fun onCityClicked(airQaulityDataModel: AirQaulityDataModel) {
        mainViewModel.selectedCityLiveData.value = airQaulityDataModel.city
        findNavController().navigate(R.id.action_homeFragment_to_graphFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mConnectivityManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkInternet()

        mainViewModel.setConnectionFailureListener(this)

        mainViewModel.connect()

        setUpRecyclerView()

        loadData()
    }

    private fun checkInternet() {
        if (!isNetworkAvailable(context)) noInternetUI()
    }

    private fun loadData() {
        mBinding?.progressBar?.visibility = View.VISIBLE
        lifecycleScope.launch {
            mainViewModel.getLatestData().collect {
                mBinding?.progressBar?.visibility = View.GONE
                mBinding?.errorText?.visibility = View.GONE
                mBinding?.aqiRecyclerView?.visibility = View.VISIBLE
                mAqiListAdapter.updateList(it)
            }
        }
    }

    private fun setUpRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(activity)
        mBinding?.aqiRecyclerView?.let {
            it.adapter = mAqiListAdapter
            it.layoutManager = linearLayoutManager
            (it.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainViewModel.setConnectionFailureListener(null)
        mBinding = null
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.connect()
        mConnectivityManager.registerNetworkCallback(networkRequest, networkListener)
    }

    override fun onPause() {
        super.onPause()
        mConnectivityManager.unregisterNetworkCallback(networkListener)
        mainViewModel.cancel()
    }

    private val networkListener = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            lifecycleScope.launch {
                mainViewModel.connect()
                mBinding?.errorText?.visibility = View.GONE
                loadData()
            }
        }

        override fun onLost(network: Network) {
            lifecycleScope.launch {
                //Show network error ui
                noInternetUI()
            }
        }
    }

    private fun noInternetUI() {
        mBinding?.progressBar?.visibility = View.GONE
        mBinding?.aqiRecyclerView?.visibility = View.GONE
        mBinding?.errorText?.visibility = View.VISIBLE
        mBinding?.errorText?.text = getString(R.string.no_internet)
    }

    override fun onSocketFailure() {
        lifecycleScope.launch {
            //Show error UI
            mBinding?.progressBar?.visibility = View.GONE
            mBinding?.aqiRecyclerView?.visibility = View.GONE
            mBinding?.errorText?.visibility = View.VISIBLE
            mBinding?.errorText?.text = getString(R.string.socket_failure)
        }
    }
}