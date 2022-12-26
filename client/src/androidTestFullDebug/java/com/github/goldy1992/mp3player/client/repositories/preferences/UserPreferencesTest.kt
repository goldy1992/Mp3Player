package com.github.goldy1992.mp3player.client.repositories.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.goldy1992.mp3player.client.data.repositories.preferences.UserPreferencesRepository
import com.github.goldy1992.mp3player.client.ui.Theme
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class UserPreferencesTest {

    private val testScheduler : TestCoroutineScheduler = TestCoroutineScheduler()
    private val dispatcher : TestDispatcher = UnconfinedTestDispatcher(testScheduler)
    private val testScope : TestScope = TestScope(dispatcher)

    private val TEST_DATASTORE_NAME = "test_datastore"
    private val testContext: Context = ApplicationProvider.getApplicationContext()
    private var testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
                                                            scope = testScope,
                                                            produceFile = { testContext.preferencesDataStoreFile(TEST_DATASTORE_NAME) }
                                                        )
    private val repository: UserPreferencesRepository = UserPreferencesRepository(testDataStore)

    @Test
    fun testDarkMode() = testScope.runTest {
        var result = false
        val collectJob = testScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.getDarkMode().collect {
                result = it
            }
        }
        runBlocking {
            repository.updateDarkMode(false)
            assertFalse(result)
            repository.updateDarkMode(true)
            assertTrue(result)
        }

        collectJob.cancel()
    }

    @Test
    fun testSystemDarkMode() = testScope.runTest {
        var result = false
        val collectJob = testScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.getSystemDarkMode().collect {
                result = it
            }
        }
        runBlocking {
            repository.updateSystemDarkMode(false)
            assertFalse(result)
            repository.updateSystemDarkMode(true)
            assertTrue(result)
        }
        collectJob.cancel()
    }

    @Test
    fun testTheme() = testScope.runTest {
        var result: Theme? = null
        val collectJob = testScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.getTheme().collect {
                result = it
            }
        }
        runBlocking {
            repository.updateTheme(Theme.BLUE)
            assertEquals(Theme.BLUE, result)
            repository.updateTheme(Theme.ORANGE)
            assertEquals(Theme.ORANGE, result)
        }

        collectJob.cancel()
    }

    @After
    fun cleanUp() {
        testScheduler.cancelChildren()
        dispatcher.cancelChildren()
        testScope.cancel()
    }
}