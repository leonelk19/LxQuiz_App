package com.example.lxquiz.domain.usecase.competition

import com.example.lxquiz.data.models.Tournament
import com.example.lxquiz.data.remote.NetworkResult
import com.example.lxquiz.domain.repository.ICompetitionRepository
import javax.inject.Inject

class GetTournamentsUseCase @Inject constructor(
    private val repository: ICompetitionRepository
) {
    suspend operator fun invoke(): NetworkResult<List<Tournament>> {
        return repository.getTournaments()
    }
}
