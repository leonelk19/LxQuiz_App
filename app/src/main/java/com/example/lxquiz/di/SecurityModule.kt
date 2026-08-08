package com.example.lxquiz.di

import com.example.lxquiz.core.security.ProctoringManager
import com.example.lxquiz.core.security.SecurityManager
import com.example.lxquiz.domain.manager.IProctoringManager
import com.example.lxquiz.domain.manager.ISecurityManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SecurityModule {

    @Binds
    @Singleton
    abstract fun bindSecurityManager(
        securityManager: SecurityManager
    ): ISecurityManager

    @Binds
    @Singleton
    abstract fun bindProctoringManager(
        proctoringManager: ProctoringManager
    ): IProctoringManager
}
