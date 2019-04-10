package org.freeandroidtools.trendinggithub.ui.login

import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.EmptyResultSetException
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import org.freeandroidtools.trendinggithub.db.AuthDao
import org.freeandroidtools.trendinggithub.db.RepoDatabase
import org.freeandroidtools.trendinggithub.db.UserDao
import org.freeandroidtools.trendinggithub.model.AuthorizedUser
import org.freeandroidtools.trendinggithub.model.User
import org.hamcrest.Matchers.greaterThan
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class AuthEntityTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var db: RepoDatabase
    private lateinit var authDao: AuthDao
    private lateinit var userDao: UserDao

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        db = Room.inMemoryDatabaseBuilder(context, RepoDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        userDao = db.userDao()
        authDao = db.authDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


    @Test(expected = SQLiteConstraintException::class)
    @Throws(Exception::class)
    fun insertNonUserTest() {
        authDao.insert(AuthorizedUser(1))
    }

    @Test
    @Throws(Exception::class)
    fun insertAuthTest() {
        val user = getUser()
        userDao = db.userDao()
        val userId = userDao.insert(user)


        val id = authDao.insert(AuthorizedUser(userId))
        Assert.assertThat("id", id, greaterThan(0L))

        authDao.getSelected()
            .test()
            .assertValue {
                it.userId == userId
            }
    }

    @Test
    @Throws(Exception::class)
    fun deleteAuthTest() {
        val user = getUser()
        userDao = db.userDao()
        val userId = userDao.insert(user)

        val id = authDao.insert(AuthorizedUser(userId))

        authDao.deleteAll()
        authDao.getSelected()
            .test()
            .assertNoValues()
            .assertError {
                it is EmptyResultSetException
            }
    }

    @Test
    @Throws(Exception::class)
    fun autoDeleteAuthWithUserTest() {

        val user = getUser()
        userDao = db.userDao()
        val userId = userDao.insert(user)

        authDao.insert(AuthorizedUser(userId))
        userDao.deleteAll()
        authDao.getSelected()
            .test()
            .assertNoValues()
            .assertError {
                it is EmptyResultSetException
            }
    }

    private fun getUser(): User {
        val user = User()
        user.apply {
            token = "123456"
            login = "login1"
            name = "name1"
            id = 123455
        }
        return user
    }
}
