package org.freeandroidtools.trendinggithub.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import org.freeandroidtools.trendinggithub.service.GithubApiService
import org.freeandroidtools.trendinggithub.model.GithubRepo
import org.freeandroidtools.trendinggithub.repository.GithubApiRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TrendingViewModel : ViewModel() {
    val trendingData: MutableLiveData<List<GithubRepo>> = MutableLiveData()
    private val repository: GithubApiRepository

    init {
        val retrofit = Retrofit.Builder()
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

        val service = retrofit.create<GithubApiService>(GithubApiService::class.java)
        repository = GithubApiRepository(service)
    }

    fun getTrending() {
        repository.getTrending(trendingData, "Android", 30)
    }

}