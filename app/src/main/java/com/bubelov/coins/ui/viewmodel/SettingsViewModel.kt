/*
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <https://unlicense.org>
 */

package com.bubelov.coins.ui.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.bubelov.coins.db.sync.DatabaseSync
import com.bubelov.coins.repository.place.PlacesRepository
import com.bubelov.coins.repository.synclogs.SyncLogsRepository
import com.bubelov.coins.util.DistanceUnitsLiveData
import com.bubelov.coins.util.PlaceNotificationManager
import com.bubelov.coins.util.SelectedCurrencyLiveData
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.util.*
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    val selectedCurrencyLiveData: SelectedCurrencyLiveData,
    private val placesRepository: PlacesRepository,
    val distanceUnitsLiveData: DistanceUnitsLiveData,
    private val databaseSync: DatabaseSync,
    private val syncLogsRepository: SyncLogsRepository,
    private val placeNotificationsManager: PlaceNotificationManager
) : ViewModel() {
    fun getCurrencySelectorRows(): LiveData<List<CurrencySelectorRow>> =
        Transformations.switchMap(placesRepository.all(), { places ->
            val result = MutableLiveData<List<CurrencySelectorRow>>()

            if (places == null) {
                return@switchMap result
            }

            val currencies = mutableSetOf<String>()

            places.forEach {
                it.currencies.forEach {
                    currencies.add(it)
                }
            }

            result.value = currencies.map { currency ->
                CurrencySelectorRow(
                    currency,
                    places.filter { it.currencies.contains(currency) }.size
                )
            }.sortedByDescending { it.places }

            result
        })

    fun syncDatabase() = launch {
        databaseSync.sync()
    }

    fun getSyncLogs(): LiveData<List<String>> {
        val result = MutableLiveData<List<String>>()

        launch {
            result.postValue(syncLogsRepository.all()
                .reversed()
                .map { "Date: ${Date(it.time)}, Affected places: ${it.affectedPlaces}" }
            )
        }

        return result
    }

    suspend fun testNotification() {
        val randomPlace = async { placesRepository.findRandom() }.await()

        if (randomPlace != null) {
            placeNotificationsManager.issueNotification(randomPlace)
        }
    }

    data class CurrencySelectorRow(val currency: String, val places: Int)
}