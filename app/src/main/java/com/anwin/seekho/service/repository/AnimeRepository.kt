package com.anwin.seekho.service.repository

import android.util.Log
import com.anwin.seekho.service.local.db.dao.AnimeDao
import com.anwin.seekho.service.local.db.entity.AnimeEntity
import com.anwin.seekho.service.remote.api.ApiService
import com.anwin.seekho.utils.NetworkUtil

class AnimeRepository(
    private val api: ApiService,
    private val dao: AnimeDao,
    private val networkUtil: NetworkUtil
) {

    val TAG = "AnimeRepository"

    fun getAnimeFromDb() = dao.getAllAnime()

    fun getAnimeById(animeId: Int) = dao.getAnimeById(animeId)

    suspend fun fetchAndUpdateAnimeById(animeId: Int): Result<AnimeEntity> {
        return try {
            if (!networkUtil.isNetworkAvailable()) {
                return Result.failure(Exception("No network connection available"))
            }

            val response = api.getAnimeById(animeId)

            if (response.isSuccessful) {
                response.body()?.let { animeResponse ->
                    animeResponse.data?.let { dataItem ->
                        if (dataItem.malId == null || dataItem.malId == 0) {
                            return Result.failure(Exception("Invalid anime ID"))
                        }

                        // Extract genres
                        val genreNames = dataItem.genres?.mapNotNull { genre -> genre?.name }?.joinToString(" • ") ?: ""

                        val animeEntity = AnimeEntity(
                            id = dataItem.malId,
                            title = dataItem.title ?: "Unknown Title",
                            titleEnglish = dataItem.titleEnglish,
                            trailerUrl  = dataItem.trailer?.embedUrl,
                            titleJapanese = dataItem.titleJapanese,
                            imageUrl = dataItem.images?.jpg?.imageUrl,
                            episodes = dataItem.episodes,
                            rating = dataItem.score,
                            ageRating = dataItem.rating,
                            synopsis = dataItem.synopsis,
                            type = dataItem.type,
                            status = dataItem.status,
                            source = dataItem.source,
                            duration = dataItem.duration,
                            year = dataItem.year,
                            season = dataItem.season,
                            rank = dataItem.rank,
                            popularity = dataItem.popularity,
                            members = dataItem.members,
                            favorites = dataItem.favorites,
                            scoredBy = dataItem.scoredBy,
                            url = dataItem.url,
                            background = dataItem.background,
                            genres = genreNames
                        )

                        // Insert or update in database
                        dao.insertAnime(listOf(animeEntity))
                        
                        Log.d(TAG, "Successfully fetched and updated anime: ${animeEntity.title}")
                        Result.success(animeEntity)
                    } ?: Result.failure(Exception("Anime data is null"))
                } ?: Result.failure(Exception("Response body is null"))
            } else {
                Result.failure(Exception("API request failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching anime by ID: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun clearDatabase() {
        dao.clearAll()
    }

    suspend fun debugDatabase() {
        try {
            val count = dao.getAnimeCount()
            Log.d(TAG, "Total anime in database: $count")
            
            // Get all anime and log their details
            dao.getAllAnime().collect { animeList ->
                Log.d(TAG, "Debug - Retrieved ${animeList.size} anime from database:")
                animeList.forEachIndexed { index, anime ->
                    Log.d(TAG, "  [$index] ID: ${anime.id}, Title: ${anime.title}")
                }
                return@collect // Only collect once for debugging
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error debugging database: ${e.message}")
        }
    }

    suspend fun syncAnime(): Result<Unit> {
        return try {
            if (!networkUtil.isNetworkAvailable()) {
                return Result.failure(Exception("No network connection available"))
            }
            
            val response = api.getTopAnime()

            if (response.isSuccessful) {
                response.body()?.let { topAnimeResponse ->

                    val animeList = topAnimeResponse.data?.mapNotNull { dataItem ->
                        dataItem?.let {

                            if (it.malId == null || it.malId == 0) {
                                return@mapNotNull null
                            }
                            
                            // Extract genres
                            val genreNames = it.genres?.mapNotNull { genre -> genre?.name }?.joinToString(" • ") ?: ""
                            
                            AnimeEntity(
                                id = it.malId,
                                title = it.title ?: "Unknown Title",
                                titleEnglish = it.titleEnglish,
                                titleJapanese = it.titleJapanese,
                                imageUrl = it.images?.jpg?.imageUrl,
                                episodes = it.episodes,
                                rating = it.score,
                                ageRating = it.rating,
                                synopsis = it.synopsis,
                                type = it.type,
                                status = it.status,
                                source = it.source,
                                duration = it.duration,
                                year = it.year,
                                season = it.season,
                                rank = it.rank,
                                popularity = it.popularity,
                                members = it.members,
                                favorites = it.favorites,
                                scoredBy = it.scoredBy,
                                url = it.url,
                                background = it.background,
                                genres = genreNames,
                                trailerUrl  = dataItem.trailer?.embedUrl,
                            )
                        }
                    } ?: emptyList()

                    if (animeList.isNotEmpty()) {
                        dao.clearAll()
                        dao.insertAnime(animeList)
                    } else {
                        Log.w(TAG, "No anime to insert - list is empty")
                    }
                } ?: Log.e(TAG, "Response body is null")
            } else {
                return Result.failure(Exception("API request failed with code: ${response.code()}"))
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    // Check if network is available
    fun isNetworkAvailable(): Boolean {
        return networkUtil.isNetworkAvailable()
    }

    // Api call for direct API access (if needed)
    suspend fun getAnimeList() = if (networkUtil.isNetworkAvailable()) {
        api.getTopAnime()
    } else {
        Log.w(TAG, "No network connection for direct API call")
        null
    }
}