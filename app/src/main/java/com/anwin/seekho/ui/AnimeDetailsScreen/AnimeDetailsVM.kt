package com.anwin.seekho.ui.AnimeDetailsScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anwin.seekho.service.local.db.entity.AnimeEntity
import com.anwin.seekho.service.repository.AnimeRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AnimeDetailsVM(
    private val repository: AnimeRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<AnimeDetailsUiState>()
    val uiState: LiveData<AnimeDetailsUiState> = _uiState

    fun start(animeId: Int) {
        observeAnime(animeId)
        refreshAnime(animeId)
    }

    private fun observeAnime(animeId: Int) {
        viewModelScope.launch {
            repository.getAnimeById(animeId)
                .catch {
                    _uiState.postValue(
                        AnimeDetailsUiState.Error("DB error")
                    )
                }
                .collect { anime ->
                    if (anime != null) {
                        _uiState.postValue(
                            AnimeDetailsUiState.Success(anime)
                        )
                    }
                }
        }
    }

    private fun refreshAnime(animeId: Int) {
        viewModelScope.launch {
            _uiState.postValue(AnimeDetailsUiState.Loading)
            repository.fetchAndUpdateAnimeById(animeId)
            // NO UI update here
            // DB update will trigger observeAnime()
        }
    }
}


sealed class AnimeDetailsUiState {
    object Loading : AnimeDetailsUiState()
    data class Success(val anime: AnimeEntity) : AnimeDetailsUiState()
    data class Error(val message: String) : AnimeDetailsUiState()
}