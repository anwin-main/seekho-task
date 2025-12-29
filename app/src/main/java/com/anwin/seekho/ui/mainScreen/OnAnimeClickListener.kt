package com.anwin.seekho.ui.mainScreen

import com.anwin.seekho.service.local.db.entity.AnimeEntity

interface OnAnimeClickListener {
    fun onAnimeClick(item: AnimeEntity, position: Int)
}
