package com.anwin.seekho.data

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class AnimeResponse(
    val data: DataItem? = null
)

@Serializable
data class TopAnimeResponse(
	val pagination: Pagination? = null,
	val data: List<DataItem?>? = null
)

@Serializable
data class StudiosItem(
	val name: String? = null,
	val malId: Int? = null,
	val type: String? = null,
	val url: String? = null
)

@Serializable
data class To(
	val month: Int? = null,
	val year: Int? = null,
	val day: Int? = null
)

@Serializable
data class LicensorsItem(
	val name: String? = null,
	val malId: Int? = null,
	val type: String? = null,
	val url: String? = null
)

@Serializable
data class Prop(
	val from: From? = null,
	val to: To? = null
)

@Serializable
data class From(
	val month: Int? = null,
	val year: Int? = null,
	val day: Int? = null
)

@Serializable
data class DataItem(
	val titleJapanese: String? = null,
	val favorites: Int? = null,
	val broadcast: Broadcast? = null,
	val year: Int? = null,
    @SerializedName("rating")
	val rating: String? = null,
	val scoredBy: Int? = null,
	val titleSynonyms: List<String?>? = null,
	val source: String? = null,
	val title: String? = null,
	val type: String? = null,
	val trailer: Trailer? = null,
	val duration: String? = null,
	val score: Double? = null,
	val themes: List<ThemesItem?>? = null,
	val approved: Boolean? = null,
	val genres: List<GenresItem?>? = null,
	val popularity: Int? = null,
	val members: Int? = null,
	val titleEnglish: String? = null,
	val rank: Int? = null,
	val season: String? = null,
	val airing: Boolean? = null,
	val episodes: Int? = null,
	val aired: Aired? = null,
	val images: Images? = null,
	val studios: List<StudiosItem?>? = null,
    @SerializedName("mal_id")
    val malId: Int? = null,
	val titles: List<TitlesItem?>? = null,
	val synopsis: String? = null,
	val explicitGenres: List<GenresItem?>? = null,
	val licensors: List<LicensorsItem?>? = null,
	val url: String? = null,
	val producers: List<ProducersItem?>? = null,
	val background: String? = null,
	val status: String? = null,
	val demographics: List<DemographicsItem?>? = null
)

@Serializable
data class GenresItem(
	val name: String? = null,
	val malId: Int? = null,
	val type: String? = null,
	val url: String? = null
)

@Serializable
data class Aired(
	val string: String? = null,
	val prop: Prop? = null,
	val from: String? = null,
	val to: String? = null
)

@Serializable
data class TitlesItem(
	val type: String? = null,
	val title: String? = null
)

@Serializable
data class Pagination(
	val hasNextPage: Boolean? = null,
	val lastVisiblePage: Int? = null,
	val items: Items? = null,
	val currentPage: Int? = null
)

@Serializable
data class Webp(
	val largeImageUrl: String? = null,
	val smallImageUrl: String? = null,
	val imageUrl: String? = null
)

@Serializable
data class DemographicsItem(
	val name: String? = null,
	val malId: Int? = null,
	val type: String? = null,
	val url: String? = null
)

@Serializable
data class Broadcast(
	val string: String? = null,
	val timezone: String? = null,
	val time: String? = null,
	val day: String? = null
)

@Serializable
data class ProducersItem(
	val name: String? = null,
	val malId: Int? = null,
	val type: String? = null,
	val url: String? = null
)

@Serializable
data class Items(
	val perPage: Int? = null,
	val total: Int? = null,
	val count: Int? = null
)

@Serializable
data class Trailer(
	val images: TrailerImages? = null,
    @SerializedName("embed_url")
	val embedUrl: String? = null,
    @SerializedName("youtube_id")
	val youtubeId: String? = null,
	val url: String? = null
)

@Serializable
data class Images(
	val jpg: Jpg? = null,
	val webp: Webp? = null
)

@Serializable
data class TrailerImages(
	val largeImageUrl: String? = null,
	val smallImageUrl: String? = null,
	val imageUrl: String? = null,
	val mediumImageUrl: String? = null,
	val maximumImageUrl: String? = null
)

@Serializable
data class Jpg(
	val largeImageUrl: String? = null,
	val smallImageUrl: String? = null,
    @SerializedName("image_url")
	val imageUrl: String? = null
)

@Serializable
data class ThemesItem(
	val name: String? = null,
	val malId: Int? = null,
	val type: String? = null,
	val url: String? = null
)

