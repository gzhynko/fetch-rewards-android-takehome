package com.gzhynko.fetch_takehome.ui

import android.net.http.HttpException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gzhynko.fetch_takehome.model.HiringDataEntry
import com.gzhynko.fetch_takehome.network.HiringDataApi
import com.gzhynko.fetch_takehome.network.HiringDataApiService
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

/**
 * UI state for the app.
 */
sealed interface FetchTakehomeUiState {
    data class Success(val groupedData: SortedMap<Int, List<HiringDataEntry>>) : FetchTakehomeUiState
    data object Error : FetchTakehomeUiState
    data object LoadingData : FetchTakehomeUiState
}

/**
 * The ViewModel for the app. Updates the UI state based on the result of the data download.
 */
class FetchTakehomeViewModel : ViewModel() {
    var fetchTakehomeUiState: FetchTakehomeUiState by mutableStateOf(FetchTakehomeUiState.LoadingData)
        private set

    private val hiringDataApi: HiringDataApiService = HiringDataApi()

    /**
     * Call getHiringData() on init so we can display the loading screen on launch.
     */
    init {
        getHiringData()
    }

    /**
     * Makes a request to the API service to fetch data from the Fetch API and
     * updates the UI state based on whether the request was successful.
     */
    fun getHiringData() {
        viewModelScope.launch {
            fetchTakehomeUiState = FetchTakehomeUiState.LoadingData
            fetchTakehomeUiState = try {
                val listResult = hiringDataApi.getData()
                FetchTakehomeUiState.Success(listResult)
            } catch (e: IOException) {
                FetchTakehomeUiState.Error
            } catch (e: HttpException) {
                FetchTakehomeUiState.Error
            }
        }
    }
}
