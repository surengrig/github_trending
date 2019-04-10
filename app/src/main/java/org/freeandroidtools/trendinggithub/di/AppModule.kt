package org.freeandroidtools.trendinggithub.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.freeandroidtools.trendinggithub.BuildConfig
import org.freeandroidtools.trendinggithub.TrendingApp
import org.freeandroidtools.trendinggithub.db.RepoDatabase
import org.freeandroidtools.trendinggithub.service.GithubApiService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
class AppModule(private val app: TrendingApp) {

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
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
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {

        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .addInterceptor(BaseInterceptor())
            .addInterceptor(logging)
            .build()

    }

    private inner class BaseInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()

            Timber.d("request chain: token = ${app.token}")
            //add access token
            app.token?.let {
                val auth = if (it.startsWith("Basic")) it else "token $it"
                request = request.newBuilder()
                    .addHeader("Authorization", auth)
                    .build()
            }

            return chain.proceed(request)
        }
    }

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

    @Provides
    @Singleton
    fun provideApplication(): TrendingApp = app

}