package com.example.lxquiz.domain.usecase.competition

import com.example.lxquiz.data.models.LeaderboardEntry
import com.example.lxquiz.data.remote.NetworkResult
import com.example.lxquiz.domain.repository.ICompetitionRepository
import javax.inject.Inject

class GetLeaderboardUseCase @Inject constructor(
    private val repository: ICompetitionRepository
) {
    suspend operator fun invoke(type: String, region: String? = null): NetworkResult<List<LeaderboardEntry>> {
        return repository.getLeaderboard(type, region)
    }
}
