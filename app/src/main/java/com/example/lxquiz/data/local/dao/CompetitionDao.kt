package com.example.lxquiz.data.local.dao

import androidx.room.*
import com.example.lxquiz.data.local.entities.CompetitionResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CompetitionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: CompetitionResultEntity): Unit

    @Query("SELECT * FROM offline_competition_results WHERE isSynced = 0")
    suspend fun getUnsyncedResults(): List<CompetitionResultEntity>

    @Query("UPDATE offline_competition_results SET isSynced = 1 WHERE id IN (:ids)")
    suspend fun markAsSynced(ids: List<Int>): Unit

    @Query("SELECT SUM(score) FROM offline_competition_results WHERE userId = :userId")
    fun getTotalOfflineScore(userId: String): Flow<Int?>
}
