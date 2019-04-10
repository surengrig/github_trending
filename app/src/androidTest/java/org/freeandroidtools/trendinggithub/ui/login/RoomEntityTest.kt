package org.freeandroidtools.trendinggithub.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import org.freeandroidtools.trendinggithub.db.AuthDao
import org.freeandroidtools.trendinggithub.db.RepoDatabase
import org.freeandroidtools.trendinggithub.db.UserDao
import org.freeandroidtools.trendinggithub.model.User
import org.hamcrest.Matchers.greaterThan
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException
import java.util.Collections

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class RoomEntityTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var observer: Observer<List<User>>

    private lateinit var db: RepoDatabase
    private lateinit var userDao: UserDao
    private lateinit var authDao: AuthDao

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

    @Test
    @Throws(Exception::class)
    fun insertUserTest() {
        val user = User()
        user.apply {
            token = "55555"
        }
        userDao = db.userDao()
        val live = userDao.getAll()
        live.observeForever(observer)
        val id = userDao.insert(user)

        Assert.assertThat("id", id, greaterThan(0L))

        Mockito.verify(observer).onChanged(Collections.singletonList(user))
    }
}
