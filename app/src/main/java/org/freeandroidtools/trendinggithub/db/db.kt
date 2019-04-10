package org.freeandroidtools.trendinggithub.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import org.freeandroidtools.trendinggithub.model.AuthorizedUser
import org.freeandroidtools.trendinggithub.model.DateConverter
import org.freeandroidtools.trendinggithub.model.GithubRepo
import org.freeandroidtools.trendinggithub.model.StarredRepo
import org.freeandroidtools.trendinggithub.model.User

@Database(
    entities = [StarredRepo::class, GithubRepo::class, AuthorizedUser::class, User::class],
    exportSchema = false,
    version = 8
)
@TypeConverters(DateConverter::class)
abstract class RepoDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
    abstract fun starredReposDao(): StarredRepoDao
    abstract fun authDao(): AuthDao
    abstract fun userDao(): UserDao
}

@Dao
interface StarredRepoDao {
    @Insert
    fun insert(repo: StarredRepo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(repos: List<StarredRepo>)

    @Query("DELETE FROM starredRepo")
    fun deleteAll()

    @Query("SELECT * FROM starredRepo WHERE id = :id")
    fun getById(id: String): LiveData<List<StarredRepo>>

    @Query("SELECT * FROM starredRepo")
    fun getAll(): Flowable<List<StarredRepo>>
}

@Dao
interface RepoDao {

    @Insert
    fun insert(repo: GithubRepo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(repos: List<GithubRepo>)

    @Query("DELETE FROM repo")
    fun deleteAll()

    @Query("SELECT * FROM repo WHERE id = :id")
    fun getById(id: String): LiveData<List<GithubRepo>>

    @Query("SELECT * FROM repo")
    fun getAll(): Flowable<List<GithubRepo>>
}

@Dao
interface AuthDao {
    @Insert
    fun insert(user: AuthorizedUser): Long

    @Query("DELETE FROM AuthorizedUser")
    fun deleteAll()

    @Query("SELECT * FROM AuthorizedUser LIMIT 1")
    fun getSelected(): Single<AuthorizedUser>
}

@Dao
interface UserDao {
    @Insert
    fun insert(user: User): Long

    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT * FROM user")
    fun getAllNonLive(): List<User>


    @Query("SELECT * FROM user WHERE dbId= :id")
    fun getById(id: Long): Maybe<List<User>>

    @Delete
    fun delete(user: User)

    @Query("DELETE FROM user WHERE id= :id")
    fun deleteById(id: Long)

    @Query("DELETE FROM user")
    fun deleteAll(): Int
}