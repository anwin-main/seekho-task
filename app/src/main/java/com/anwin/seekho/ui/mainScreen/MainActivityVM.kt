package com.anwin.seekho.ui.mainScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anwin.seekho.service.local.db.entity.AnimeEntity
import com.anwin.seekho.service.repository.AnimeRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MainActivityVM(private val repository: AnimeRepository) : ViewModel() {

    val TAG = "MainActivityVM"

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    init {
        observeAnimeFromDb()
    }

    private fun observeAnimeFromDb() {
        viewModelScope.launch {
            repository.getAnimeFromDb()
                .catch { e -> _uiState.value = UiState.Error(e.message ?: "Unknown error") }
                .collect { animeEntities ->
                    _uiState.value = UiState.Success(animeEntities)
                }
        }
    }

    fun loadAnimeList() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            try {
                Log.d(TAG, "Starting to sync anime from API")
                val result = repository.syncAnime()
                
                result.fold(
                    onSuccess = {
                        Log.d(TAG, "Sync completed successfully")
                    },
                    onFailure = { exception ->
                        Log.e(TAG, "Sync failed: ${exception.message}")
                        _uiState.value = UiState.Error(exception.message ?: "Unknown error occurred")
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error syncing anime: ${e.message}")
                _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun refreshData() {
        loadAnimeList()
    }
}

sealed class UiState {
    object Loading : UiState()
    data class Success(val animeList: List<AnimeEntity>) : UiState()
    data class Error(val message: String) : UiState()
}
//
//fun clearAndReload() {
//    viewModelScope.launch {
//        _uiState.value = UiState.Loading
//        try {
//            Log.d(TAG, "Clearing database and reloading...")
//            repository.clearDatabase()
//            repository.syncAnime()
//        } catch (e: Exception) {
//            Log.e(TAG, "Error in clearAndReload: ${e.message}")
//            _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
//        }
//    }
//}
//
//fun retryWithNetworkCheck() {
//    if (repository.isNetworkAvailable()) {
//        loadAnimeList()
//    } else {
//        _uiState.value = UiState.Error("No network connection available")
//    }
//}