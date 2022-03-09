package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    // Added insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertElection(vararg election: Election)

    // Added select all election query
    @Query("SELECT * FROM election_table ORDER BY electionDay DESC")
    fun getAllElections(): LiveData<List<Election>>

    // Added select single election query
    @Query("SELECT * FROM election_table WHERE id = :id")
    fun getElection(id: Int): Election

    // Added delete query
    @Query("DELETE FROM election_table WHERE id = :id")
    fun deleteElection(id: Int)

    // Add clear query
    @Query("DELETE FROM election_table")
    fun clearElection()

    // Added select followed elections
    @Query("SELECT * FROM election_table WHERE isFollowed = :followed")
    fun getFollowedElections(followed: Boolean): LiveData<List<Election>>

}