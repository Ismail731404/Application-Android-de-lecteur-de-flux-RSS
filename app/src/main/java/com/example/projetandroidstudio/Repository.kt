package com.example.projetandroidstudio

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class Repository(private val fluxDao:FluxDao,private val infoDao: InfoDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allFlux: Flow<List<Flux>> = fluxDao.getFluxs()
    val allInfo: Flow<List<Info>> = infoDao.getInfos()
    val allFluxTelecharge: Flow<List<Flux>> = fluxDao.getFluxsTelecharge(true);
    val allInfoSelected: Flow<List<Info>> =infoDao.getInfoSelected(true);



    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertFlux(flux: Flux) {
        fluxDao.insert(flux)
    }

    @WorkerThread
    suspend fun insertInfo(info: Info) {
        infoDao.insert(info)
    }

    suspend fun updateFlux(id: String, co: Boolean){
          fluxDao.updateFlux(id,co)
    }
    suspend fun updateInfo(id: String, co: Boolean){
        infoDao.updateInfo(id,co)
    }

    suspend fun deleteUnFlux(id: String){
        fluxDao.deleteUnFlux(id)
    }
    suspend fun deleteUnInfo(id: String){
        infoDao.deleteUnInfo(id)
    }



}