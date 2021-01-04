package com.example.projetandroidstudio

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "info")
class Info(
    @PrimaryKey()
    val Title:String,
    val Description:String,
    val Link:String,
    val Nouveau:Boolean = true


    )

