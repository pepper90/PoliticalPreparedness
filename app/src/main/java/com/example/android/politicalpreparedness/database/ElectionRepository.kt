package com.example.android.politicalpreparedness.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//enum class CivicApiStatus { LOADING, ERROR, DONE }

class ElectionRepository(private val database: ElectionDatabase) {

    val getAllElections: LiveData<List<Election>> = database.electionDao.getAllElections()

    val getFollowedElections: LiveData<List<Election>> = database.electionDao.getFollowedElections(true)



//    private val _status = MutableLiveData<CivicApiStatus>()
//    val status: LiveData<CivicApiStatus>
//        get() = _status

    suspend fun refreshElections() {
//        _status.value = CivicApiStatus.LOADING
        withContext(Dispatchers.IO) {
            try {
                val elections = CivicsApi.retrofitService.electionQuery().await().elections
                database.electionDao.insertElection(*elections.toTypedArray())
//                _status.value = CivicApiStatus.DONE
                Log.d(TAG, elections.toString())
            } catch (err: Exception) {
//                _status.value = CivicApiStatus.ERROR
                Log.d(TAG, err.printStackTrace().toString())
            }
        }
    }
}

private const val TAG = "ElectionRepository"