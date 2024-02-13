/*
 * Copyright (c) 2023 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perracolabs.yasc.system.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Abstract base class for grouping settings that are stored in the same file,
 * which subclasses must extend to represent concrete settings groups.
 *
 * Settings within a group share a contextual purpose and are persisted into a
 * uniquely named settings file.
 *
 * Read and write operations ensure thread-safety:
 *  - Operations are dispatched to the IO dispatcher for execution.
 *  - Operations across different settings files are executed in parallel.
 *  - Within the same settings file, operations for each unique setting key are
 *    executed sequentially to prevent race conditions.
 *
 * @param context Current context, required to open/generate the settings file.
 * @param settingsName Unique name for the settings file where all settings
 *                     items of the group are persisted.
 */
abstract class AbsSettingsGroup(context: Context, private val settingsName: String) {

    /**
     * The storage settings file.
     */
    protected val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile(name = settingsName) }
    )

    /**
     * Performs a read operation on the settings file.
     */
    protected fun <T> readOperation(settingKeyName: String, operation: suspend () -> T): Flow<T> {
        return flow {
            getMutex(settingKeyName).withLock {
                emit(operation.invoke())
            }
        }
    }

    /**
     * Performs a write operation on the settings file.
     */
    protected fun writeOperation(settingKeyName: String, operation: suspend () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            getMutex(settingKeyName).withLock {
                operation.invoke()
            }
        }
    }

    /**
     * Returns the mutex associated with the settings file, to be used to
     * discriminate if editions are don't serially or in parallel.
     */
    private fun getMutex(settingKeyName: String): Mutex {
        return mutexMap.getOrPut(key = "$settingsName $settingKeyName") { Mutex() }
    }

    companion object {
        /**
         * Map holding the mutexes for serial and parallel edit operation.
         */
        private val mutexMap = mutableMapOf<String, Mutex>()
    }
}
