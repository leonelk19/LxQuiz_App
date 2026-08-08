package com.example.lxquiz.di

import com.example.lxquiz.data.repository.ArenaRepository
import com.example.lxquiz.data.repository.AuthRepository
import com.example.lxquiz.data.repository.CompetitionRepository
import com.example.lxquiz.data.repository.UserRepository
import com.example.lxquiz.domain.repository.IArenaRepository
import com.example.lxquiz.domain.repository.IAuthRepository
import com.example.lxquiz.domain.repository.ICompetitionRepository
import com.example.lxquiz.domain.repository.IUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepository: AuthRepository
    ): IAuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepository: UserRepository
    ): IUserRepository

    @Binds
    @Singleton
    abstract fun bindCompetitionRepository(
        competitionRepository: CompetitionRepository
    ): ICompetitionRepository

    @Binds
    @Singleton
    abstract fun bindArenaRepository(
        arenaRepository: ArenaRepository
    ): IArenaRepository
}
