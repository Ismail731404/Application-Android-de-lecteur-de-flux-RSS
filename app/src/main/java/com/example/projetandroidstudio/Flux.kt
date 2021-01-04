package com.example.projetandroidstudio
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flux")
class Flux(
    @PrimaryKey
    val Source:String,
    val TAG:String,
    val URL:String,
    val coche:Boolean
   // @PrimaryKey(autoGenerate = true) val id: Int?

)