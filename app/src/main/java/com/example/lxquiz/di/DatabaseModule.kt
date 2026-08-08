package com.example.lxquiz.di

import android.content.Context
import androidx.room.Room
import com.example.lxquiz.data.local.LxQuizDatabase
import com.example.lxquiz.data.local.dao.CompetitionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LxQuizDatabase {
        return Room.databaseBuilder(
            context,
            LxQuizDatabase::class.java,
            "lxquiz_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCompetitionDao(database: LxQuizDatabase): CompetitionDao {
        return database.competitionDao()
    }
}
