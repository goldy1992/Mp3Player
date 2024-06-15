package com.github.goldy1992.mp3player.client.repositories.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.goldy1992.mp3player.client.data.repositories.preferences.UserPreferencesRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class UserPreferencesRepositoryTest {
    companion object {
        private const val TEST_DATASTORE_NAME = "test_datastore"
    }
    private val testScheduler : TestCoroutineScheduler = TestCoroutineScheduler()
    private val dispatcher : TestDispatcher = UnconfinedTestDispatcher(testScheduler)
    private val backgroundScope : TestScope = TestScope(dispatcher)
    private val testContext: Context = ApplicationProvider.getApplicationContext()
    private var testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
                                                            scope = backgroundScope,
                                                            produceFile = { testContext.preferencesDataStoreFile(TEST_DATASTORE_NAME) }
                                                        )
    private val repository: UserPreferencesRepository = UserPreferencesRepository(testDataStore)

    @Test
    fun testDarkMode() = runTest {
        var result = false
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.userPreferencesFlow() .collect {
                result = it.darkMode
            }
        }

        repository.updateDarkMode(false)
        assertFalse(result)
        repository.updateDarkMode(true)
        assertTrue(result)

    }

    @After
    fun cleanUp() {
        testScheduler.cancelChildren()
        dispatcher.cancelChildren()
        backgroundScope.cancel()
    }
}