package com.anwin.seekho.service.local.db
import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    fun provideDatabase(context: Context): AnimeDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AnimeDatabase::class.java,
            "anime_db"
        ).build()
    }
}
