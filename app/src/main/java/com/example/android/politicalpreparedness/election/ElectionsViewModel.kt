package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import kotlinx.coroutines.launch

enum class CivicApiStatus { LOADING, ERROR, DONE }

// Construct ViewModel and provide election datasource
class ElectionsViewModel(application: Application): AndroidViewModel(application) {

    private val _status = MutableLiveData<CivicApiStatus>()
    val status: LiveData<CivicApiStatus>
        get() = _status

    //Live data val for upcoming elections
    private val _upcomingElectionsResponse = MutableLiveData<ElectionResponse?>()
    val upcomingElectionsResponse: LiveData<ElectionResponse?>
        get() = _upcomingElectionsResponse

    //Live data val for saved elections
    private val _savedElectionsResponse = MutableLiveData<ElectionResponse>()
    val savedElectionsResponse: LiveData<ElectionResponse>
    get() = _savedElectionsResponse

    init {
        getElections()
    }

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    fun getElections() {
        viewModelScope.launch {
            _status.value = CivicApiStatus.LOADING
            try {
                _upcomingElectionsResponse.value = CivicsApi.retrofitService.electionQuery()
                _status.value = CivicApiStatus.DONE
            } catch (e: Exception) {
                _status.value = CivicApiStatus.ERROR
                _upcomingElectionsResponse.value = null
            }
        }
    }

    //Created functions to navigate to saved or upcoming election voter info
    private val _navigateToVoterInfo = MutableLiveData<Election?>()
    val navigateToDetailElection: LiveData<Election?>
        get() = _navigateToVoterInfo

    fun onElectionClicked(asteroid: Election) {
        _navigateToVoterInfo.value = asteroid
    }

    fun onElectionNavigated() {
        _navigateToVoterInfo.value = null
    }

}