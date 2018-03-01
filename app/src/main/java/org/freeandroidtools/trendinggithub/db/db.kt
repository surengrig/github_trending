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

    @Query("SELECT * FROM repo WHERE id = :id")
    fun getById(id: String): LiveData<List<GithubRepo>>
}

@Database(entities = [(GithubRepo::class)], exportSchema = false, version = 6)
@TypeConverters(DateConverter::class)
abstract class RepoDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
}