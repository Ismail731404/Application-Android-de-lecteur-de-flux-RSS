package com.example.projetandroidstudio

import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ViewModelProjet(private val repository: Repository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allFlux: LiveData<List<Flux>> = repository.allFlux.asLiveData()
    val allInfos: LiveData<List<Info>> = repository.allInfo.asLiveData()
    val allFluxTelecharge: LiveData<List<Flux>> = repository.allFluxTelecharge.asLiveData()
    val allInfoSelected:   LiveData<List<Info>> = repository.allInfoSelected.asLiveData()
    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insertFlux(flux: Flux) = viewModelScope.launch {
        repository.insertFlux(flux)
    }
    fun insertInfo(info: Info) = viewModelScope.launch {
        repository.insertInfo(info)
    }
    fun updateFlux(id: String, co: Boolean)= viewModelScope.launch{
        repository.updateFlux(id,co)
    }
    fun updateInfo(id: String, co: Boolean)= viewModelScope.launch{
        repository.updateInfo(id,co)
    }
    fun deleteUnFlux(id: String)= viewModelScope.launch{
        repository.deleteUnFlux(id)
    }
    fun deleteUnInfo(id: String)= viewModelScope.launch{
        repository.deleteUnInfo(id)
    }



}

class WordViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelProjet::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewModelProjet(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}