package com.anwin.seekho.service.local.db.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "anime")
data class AnimeEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val titleEnglish: String?,
    val titleJapanese: String?,
    val trailerUrl: String?,
    val imageUrl: String?,
    val episodes: Int?,
    val rating: Double?,
    val ageRating: String?,
    val synopsis: String?,
    val type: String?,
    val status: String?,
    val source: String?,
    val duration: String?,
    val year: Int?,
    val season: String?,
    val rank: Int?,
    val popularity: Int?,
    val members: Int?,
    val favorites: Int?,
    val scoredBy: Int?,
    val url: String?,
    val background: String?,
    val genres: String? // Store as comma-separated string
)