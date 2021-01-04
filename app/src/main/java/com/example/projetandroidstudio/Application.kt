package com.example.projetandroidstudio

import android.app.Application
import com.example.projetandroidstudio.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class Application : Application() {


    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { FluxDatabase.getDatabase(this) }
    val repository by lazy { Repository(database.fluxDao(),database.infoDao()) }
}