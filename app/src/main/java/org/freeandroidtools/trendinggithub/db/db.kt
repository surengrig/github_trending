package org.freeandroidtools.trendinggithub.db

import android.arch.persistence.room.*
import org.freeandroidtools.trendinggithub.model.DateConverter
import org.freeandroidtools.trendinggithub.model.GithubRepo


@Dao
interface RepoDao {

    @Query("SELECT * FROM repo")
    fun getAll(): List<GithubRepo>

    @Insert
    fun insert(repo: GithubRepo)


    @Insert
    fun insertAll(repos: List<GithubRepo>)
}

@Database(entities = [(GithubRepo::class)], exportSchema = false, version = 1)
@TypeConverters(DateConverter::class)
abstract class RepoDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
}