package com.example.projetandroidstudio

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.projetandroidstudio.Flux
import kotlinx.coroutines.flow.Flow


@Dao
interface InfoDao {

    @Query("SELECT * FROM info")
    fun getInfos(): Flow<List<Info>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(info: Info)

    @Query("DELETE FROM info")
    suspend fun deleteAll()

    @Query("SELECT * FROM Info WHERE Nouveau= :co ")
    fun getInfoSelected(co: Boolean): Flow<List<Info>>

    @Query("DELETE FROM Info WHERE Title=:id")
    suspend fun deleteUnInfo(id:String)

    @Query("UPDATE Info SET Nouveau = :co WHERE Link = :id")
    fun updateInfo(id: String, co: Boolean)
}