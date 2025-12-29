package com.anwin.seekho

import android.app.Application
import androidx.room.Room
import com.anwin.seekho.service.local.db.AnimeDatabase
import com.anwin.seekho.service.remote.api.ApiClient
import com.anwin.seekho.service.remote.api.ApiService
import com.anwin.seekho.service.repository.AnimeRepository
import com.anwin.seekho.utils.NetworkUtil

class MyApplication: Application() {
    
    // Network Utility
    val networkUtil by lazy {
        NetworkUtil(applicationContext)
    }
    
    // Database
    val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            AnimeDatabase::class.java,
            "anime_database"
        )
        .fallbackToDestructiveMigration() // This will recreate the database when schema changes
        .build()
    }
    
    // API Service
    val apiService: ApiService by lazy {
        ApiClient.retrofit.create(ApiService::class.java)
    }
    
    // Repository
    val animeRepository by lazy {
        AnimeRepository(apiService, database.animeDao(), networkUtil)
    }
}