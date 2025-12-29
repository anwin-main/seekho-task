package com.anwin.seekho.ui.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anwin.seekho.service.repository.AnimeRepository

class MainActivityVMFactory(
    private val repository: AnimeRepository
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityVM::class.java)) {
            return MainActivityVM(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}