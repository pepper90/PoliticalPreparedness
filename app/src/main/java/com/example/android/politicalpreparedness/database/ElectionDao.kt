package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.Followed

@Dao
interface ElectionDao {

    /**
     * ELECTION [Election::class]
     */

    // Added insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertElection(vararg election: Election)

    // Added select all election query
    @Query("SELECT * FROM election_table ORDER BY electionDay DESC")
    fun getAllElections(): LiveData<List<Election>>

    // Added select followed elections
    @Query("SELECT * from election_table where id in (SELECT id FROM followed_table) ORDER BY electionDay DESC")
    fun getFollowedElections(): LiveData<List<Election>>

    // Added select single election query
    @Query("SELECT * FROM election_table WHERE id = :id")
    fun getElection(id: Int): Election

    // Added delete query
    @Query("DELETE FROM election_table WHERE id = :id")
    fun deleteElection(id: Int)

    // Add clear query
    @Query("DELETE FROM election_table")
    fun clearElection()

    /**
     * FOLLOWED ELECTION [Followed::class]
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollowed(followed: Followed)

    @Query("SELECT count(*) FROM followed_table where id = :id")
    fun getFollow(id: Int): Int

    @Query("DELETE FROM followed_table where id = :id")
    suspend fun deleteFollowed(id: Int)

    @Query("DELETE FROM followed_table")
    fun clearFollowed()

}