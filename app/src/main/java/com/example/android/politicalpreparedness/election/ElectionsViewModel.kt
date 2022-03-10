package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.database.ElectionRepository
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import kotlinx.coroutines.launch


// Construct ViewModel and provide election datasource
class ElectionsViewModel(application: Application): AndroidViewModel(application) {

    private val database = ElectionDatabase.getDatabase(application)
    private val electionRepository = ElectionRepository(database)

    init {
        viewModelScope.launch {
            electionRepository.refreshElections()
        }
    }

    //Live data val for upcoming elections
    val upcomingElections: LiveData<List<Election>>
        get() = electionRepository.getAllElections

    //Live data val for saved elections
    val followedElections: LiveData<List<Election>>
        get() = electionRepository.getFollowedElections

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