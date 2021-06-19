package com.cornershop.counterstest.welcome.data

import com.cornershop.counterstest.utils.data.DataStore
import io.reactivex.Single
import javax.inject.Inject

class WelcomeInfrastructure @Inject constructor(
    private val dataStore: DataStore
) : WelcomeService {

    override fun isFirstTime(): Single<Boolean> = Single.create { emitter ->
        val isFirstTime = dataStore.get(IS_FIRST_TIME_KEY) ?: true
        if (isFirstTime) {
            dataStore.put(IS_FIRST_TIME_KEY, false)
        }
        emitter.onSuccess(isFirstTime)
    }

    companion object {
        private const val IS_FIRST_TIME_KEY = "is_first_time"
    }
}