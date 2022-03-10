package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionRepository
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.launch

class VoterInfoViewModel(
    private val repository: ElectionRepository,
    private val election: Election
) : ViewModel() {



    // Add live data to hold voter info
    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo


    private val _followButtonState = MutableLiveData(false)
    val followButtonState: LiveData<Boolean>
        get() = _followButtonState

    //TODO: Add var and methods to populate voter info
    init {
        // Populate initial state of save button to reflect proper action based on election saved status
        viewModelScope.launch {
            _followButtonState.value = repository.isFollowed(election)
        }
    }

    //TODO: Add var and methods to support loading URLs


    // Added method to save and remove elections to local database
    fun onFollowClick() {
        viewModelScope.launch {
            if (_followButtonState.value!!) {
                repository.deleteFollowed(election)
            } else {
                repository.insertFollowed(election)
            }
            _followButtonState.value = repository.isFollowed(election)
        }
    }
    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}