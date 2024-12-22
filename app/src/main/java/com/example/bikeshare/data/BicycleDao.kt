package com.example.bikeshare.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BicycleDao {

    @Query("SELECT * FROM bicycles ORDER BY is_found")
    fun getAllBicycles(): Flow<List<Bicycle>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBicycles(bicycles: List<Bicycle>)

    // Add a new bicycle
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBicycle(bicycle: Bicycle)

    @Query("SELECT COUNT(*) FROM bicycles")
    suspend fun getBicycleCount(): Int

    @Query("SELECT * FROM bicycles WHERE is_Found = 0")
    fun getBicyclesNotFound(): Flow<List<Bicycle>>

    @Update
    suspend fun updateBicycle(bicycle: Bicycle)

    @Query("DELETE FROM bicycles")
    suspend fun deleteAllBicycles()
}
