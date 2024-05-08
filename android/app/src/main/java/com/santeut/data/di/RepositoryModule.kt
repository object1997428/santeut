package com.santeut.data.di

import com.santeut.data.repository.AuthRepository
import com.santeut.data.repository.AuthRepositoryImpl
import com.santeut.data.repository.PostRepository
import com.santeut.data.repository.PostRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
    @Binds
    abstract fun bindPostRepository(impl: PostRepositoryImpl): PostRepository
}