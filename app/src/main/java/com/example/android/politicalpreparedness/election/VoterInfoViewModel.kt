package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionRepository
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.utils.formatDivision
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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


    init {
        // Populate initial state of save button to reflect proper action based on election saved status
        viewModelScope.launch {
            loadVoterInfoData()
            _followButtonState.value = repository.isFollowed(election)
        }
    }
    // Added var and methods to populate voter info
    private suspend fun loadVoterInfoData() {
        try {
            withContext(Dispatchers.Main) {
                val division = formatDivision(election.division)
                _voterInfo.postValue(CivicsApi.retrofitService.voterInfoQuery(division, election.id))
            }
        } catch (err: Exception) {
            Log.d(TAG, err.printStackTrace().toString())
        }
    }

    // Add var and methods to support loading URLs
    private val _redirect = MutableLiveData<String?>()
    val redirect: LiveData<String?>
        get() = _redirect

    fun loadURLs(uri: String?) {
        _redirect.value = uri
    }

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
}

private const val TAG = "VoterInfoViewModel"
