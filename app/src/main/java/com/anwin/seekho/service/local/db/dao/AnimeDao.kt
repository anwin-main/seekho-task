package com.anwin.seekho.service.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anwin.seekho.service.local.db.entity.AnimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {

    @Query("SELECT * FROM anime ORDER BY rating DESC")
    fun getAllAnime(): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM anime WHERE id = :animeId")
    fun getAnimeById(animeId: Int): Flow<AnimeEntity?>

    @Query("SELECT COUNT(*) FROM anime")
    suspend fun getAnimeCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(list: List<AnimeEntity>)

    @Query("DELETE FROM anime")
    suspend fun clearAll()
}