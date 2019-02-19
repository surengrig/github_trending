package org.freeandroidtools.trendinggithub.di

import android.app.Application
import androidx.room.Room
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import org.freeandroidtools.trendinggithub.db.RepoDatabase
import org.freeandroidtools.trendinggithub.service.GithubApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

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
    fun provideRepoDatabase(): RepoDatabase =
            Room.databaseBuilder(app.applicationContext, RepoDatabase::class.java, "repos_db")
                    .fallbackToDestructiveMigration()
                    .build()

    @Provides
    @Singleton
    fun provideSharedPrefs(): SharedPreferences =
            app.getSharedPreferences("config", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideExecutor(): Executor = Executors.newFixedThreadPool(2)
}