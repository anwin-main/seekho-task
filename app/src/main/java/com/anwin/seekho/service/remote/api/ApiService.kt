package com.anwin.seekho.service.remote.api

import com.anwin.seekho.data.AnimeResponse
import com.anwin.seekho.data.TopAnimeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("top/anime")
    suspend fun getTopAnime(): Response<TopAnimeResponse>

    @GET("anime/{animeId}")
    suspend fun getAnimeById(@Path("animeId") animeId: Int): Response<AnimeResponse>
}
//https://api.jikan.moe/v4/top/anime
//https://api.jikan.moe/v4/anime/57555