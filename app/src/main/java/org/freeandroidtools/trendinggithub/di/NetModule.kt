package org.freeandroidtools.trendinggithub.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import org.freeandroidtools.trendinggithub.repository.GithubApiRepository
import org.freeandroidtools.trendinggithub.service.GithubApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
            Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(
                            GsonConverterFactory.create(
                                    GsonBuilder()
                                            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                                            .setPrettyPrinting()
                                            .create()
                            )
                    )
                    .build()

    @Provides
    @Singleton
    fun provideGithubApiService(retrofit: Retrofit): GithubApiService =
            retrofit.create<GithubApiService>(GithubApiService::class.java)


    @Provides
    @Singleton
    fun provideGithubApiRepository(): GithubApiRepository = GithubApiRepository()

}