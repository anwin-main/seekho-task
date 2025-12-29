package com.anwin.seekho.ui.AnimeDetailsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anwin.seekho.service.repository.AnimeRepository

class AnimeDetailsVMFactory(
    private val repository: AnimeRepository
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnimeDetailsVM::class.java)) {
            return AnimeDetailsVM(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}