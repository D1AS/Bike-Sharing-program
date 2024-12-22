package com.example.bikeshare.repositories

import com.example.bikeshare.data.Bicycle
import com.example.bikeshare.data.BicycleDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BicycleRepository @Inject constructor(
    private val bicycleDao: BicycleDao
) {

    fun getBicycles(): Flow<List<Bicycle>> = bicycleDao.getAllBicycles()

    fun getBicyclesNotFound(): Flow<List<Bicycle>> = bicycleDao.getBicyclesNotFound()

    suspend fun populateDatabaseIfEmpty() {
        if (bicycleDao.getBicycleCount() == 0) {
            //Mockup data here
            //bicycleDao.insertBicycles(getInitialBicycles())
        }
    }

    suspend fun insertBicycle(bicycle: Bicycle) {
        bicycleDao.insertBicycle(bicycle)
    }

    suspend fun updateBicycle(bicycle: Bicycle) {
        bicycleDao.updateBicycle(bicycle)
    }

    suspend fun clearDatabase() {
        bicycleDao.deleteAllBicycles()
    }


//    private fun getInitialBicycles(): List<Bicycle> {
//        return listOf(
//            Bicycle("1", "123 Yonge St, Toronto", 43.6532, -79.3832, "Bike 1", false),
//            Bicycle("2", "456 Bay St, Toronto", 43.6511, -79.3783, "Bike 2", false),
//            Bicycle("3", "789 Bloor St W, Toronto", 43.6626, -79.4110, "Bike 3", false),
//            Bicycle("4", "321 Queen St W, Toronto", 43.6488, -79.3901, "Bike 4", false),
//            Bicycle("5", "654 Spadina Ave, Toronto", 43.6667, -79.4000, "Bike 5", false)
//        )
//    }
}
