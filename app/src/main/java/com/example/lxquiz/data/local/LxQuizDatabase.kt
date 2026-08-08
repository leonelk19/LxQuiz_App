package com.example.lxquiz.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lxquiz.data.local.dao.CompetitionDao
import com.example.lxquiz.data.local.entities.CompetitionResultEntity

@Database(entities = [CompetitionResultEntity::class], version = 1, exportSchema = false)
abstract class LxQuizDatabase : RoomDatabase() {
    abstract fun competitionDao(): CompetitionDao
}
