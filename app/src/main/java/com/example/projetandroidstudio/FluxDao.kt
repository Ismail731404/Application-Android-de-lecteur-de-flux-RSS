package com.example.projetandroidstudio

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface FluxDao {

    @Query("SELECT * FROM flux ")
    fun getFluxs(): Flow<List<Flux>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(flux: Flux)

    @Query("DELETE FROM flux")
    suspend fun deleteAll()

    @Query("DELETE FROM flux WHERE source=:id")
    suspend fun deleteUnFlux(id:String)

    @Query("UPDATE flux SET coche = :co WHERE source = :id")
    fun updateFlux(id: String, co: Boolean)

    @Query("SELECT * FROM flux WHERE coche= :co ")
    fun getFluxsTelecharge(co: Boolean): Flow<List<Flux>>

}