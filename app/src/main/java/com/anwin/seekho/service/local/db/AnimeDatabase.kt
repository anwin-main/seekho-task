package com.anwin.seekho.service.local.db
import androidx.room.Database
import androidx.room.RoomDatabase
import com.anwin.seekho.service.local.db.dao.AnimeDao
import com.anwin.seekho.service.local.db.entity.AnimeEntity

@Database(
    entities = [AnimeEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AnimeDatabase : RoomDatabase() {

    abstract fun animeDao(): AnimeDao
}