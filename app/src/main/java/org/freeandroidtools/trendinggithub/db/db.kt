package org.freeandroidtools.trendinggithub.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import org.freeandroidtools.trendinggithub.model.DateConverter
import org.freeandroidtools.trendinggithub.model.GithubRepo


@Dao
interface RepoDao {

    @Query("SELECT * FROM repo")
    fun getAll(): LiveData<List<GithubRepo>>

    @Insert
    fun insert(repo: GithubRepo)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(repos: List<GithubRepo>)


    @Query("DELETE FROM repo")
    fun deleteAll()
}

@Database(entities = [(GithubRepo::class)], exportSchema = false, version = 4)
@TypeConverters(DateConverter::class)
abstract class RepoDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
}