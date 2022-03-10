package com.example.android.politicalpreparedness.database

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.Followed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionRepository(private val database: ElectionDatabase) {

    // Created val and functions to populate live data for upcoming elections from the API
    // and saved elections from local database
    val getAllElections: LiveData<List<Election>> = database.electionDao.getAllElections()

    val getFollowedElections: LiveData<List<Election>> = database.electionDao.getFollowedElections()

    suspend fun insertFollowed(election: Election) {
        withContext(Dispatchers.IO) {
            database.electionDao.insertFollowed(
                Followed(election.id)
            )
        }
    }

    suspend fun deleteFollowed(election: Election) {
        withContext(Dispatchers.IO) {
            database.electionDao.deleteFollowed(election.id)
        }
    }

    suspend fun isFollowed(election: Election): Boolean {
        var flagIsFollowed: Boolean
        withContext(Dispatchers.IO) {
            val result = database.electionDao.getFollow(election.id)
            flagIsFollowed = result > 0

        }
        Log.d(TAG, "FLAG IS FOLLOW: ${election.id}: $flagIsFollowed")
        return flagIsFollowed
    }

    suspend fun refreshElections() {
        withContext(Dispatchers.IO) {
            try {
                val elections = CivicsApi.retrofitService.electionQuery().elections
                database.electionDao.insertElection(*elections.toTypedArray())
                Log.d(TAG, elections.toString())
            } catch (err: Exception) {
                Log.d(TAG, err.printStackTrace().toString())
            }
        }
    }
}

private const val TAG = "ElectionRepository"