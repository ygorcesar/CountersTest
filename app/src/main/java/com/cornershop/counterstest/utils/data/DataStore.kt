package com.cornershop.counterstest.utils.data

import android.content.SharedPreferences
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber

class DataStore(val sharedPreferences: SharedPreferences) {

    inline fun <reified T : Any> put(key: String, value: T?): Boolean {
        try {
            if (value == null) return false

            val json = Json.encodeToString(value)
            Timber.d("===========> DataStore")
            Timber.d("PUT KEY: $key - VALUE: $json")
            sharedPreferences.edit().putString(key, json).apply()
            Timber.d("===========> DataStore transaction with success!")
            return true
        } catch (e: Exception) {
            Timber.e(e, "===========> DataStore - error on PUT info with Key: $key")
            return false
        }
    }

    inline fun <reified T : Any?> get(key: String): T? {
        try {
            Timber.d("===========> DataStore")
            val json = sharedPreferences.getString(key, null)
            Timber.d("GET KEY: $key - VALUE: $json")
            if (json == null) return null
            val data = Json.decodeFromString<T>(json)
            Timber.d("===========> DataStore transaction with success!")
            return data
        } catch (e: Throwable) {
            Timber.e(e, "===========> DataStore - error on GET info with Key: $key")
            return null
        }
    }

    inline fun <reified T : Any?> get(key: String, defaultValue: T): T =
        get<T>(key) ?: defaultValue

    fun delete(key: String) = sharedPreferences.edit()
        .remove(key)
        .apply().also {
            Timber.d("===========> DataStore")
            Timber.d("DELETE KEY: $key")
            Timber.d("===========> DataStore transaction with success!")
        }
}