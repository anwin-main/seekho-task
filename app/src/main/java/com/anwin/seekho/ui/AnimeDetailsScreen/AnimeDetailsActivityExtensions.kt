package com.anwin.seekho.ui.AnimeDetailsScreen

import android.content.Context
import android.content.Intent

fun Context.startAnimeDetailsActivity(animeId: Int) {
    val intent = Intent(this, AnimeDetailsActivity::class.java)
    intent.putExtra(AnimeDetailsActivity.EXTRA_ANIME_ID, animeId)
    startActivity(intent)
}