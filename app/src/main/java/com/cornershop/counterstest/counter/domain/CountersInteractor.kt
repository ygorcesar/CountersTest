package com.cornershop.counterstest.counter.domain

import com.cornershop.counterstest.counter.data.CounterInfrastructure
import com.cornershop.counterstest.counter.data.CounterRequest
import com.cornershop.counterstest.counter.data.CounterRequestAdd
import com.cornershop.counterstest.counter.model.Counter
import io.reactivex.Single
import javax.inject.Inject

class CountersInteractor @Inject constructor(
    private val infrastructure: CounterInfrastructure
) {

    fun getCounters(
        fetchFromRemote: Boolean
    ): Single<List<Counter>> = infrastructure.getCounters(fetchFromRemote)

    fun addCounter(request: CounterRequestAdd): Single<List<Counter>> = when {
        request.title.isBlank() -> Single.error(CounterBusiness.InvalidEmptyTitle)
        request.title.length < MIN_TITLE_SIZE -> Single.error(CounterBusiness.InvalidTitleSize)
        else -> infrastructure.addCounter(request)
    }

    fun deleteCounter(request: CounterRequest): Single<List<Counter>> =
        when (request.isIdValid()) {
            true -> infrastructure.deleteCounter(request)
            false -> Single.error(CounterBusiness.InvalidId)
        }

    fun incrementCounter(request: CounterRequest): Single<List<Counter>> =
        when (request.isIdValid()) {
            true -> infrastructure.incrementCounter(request)
            false -> Single.error(CounterBusiness.InvalidId)
        }

    fun decrementCounter(request: CounterRequest): Single<List<Counter>> =
        when (request.isIdValid()) {
            true -> infrastructure.decrementCounter(request)
            false -> Single.error(CounterBusiness.InvalidId)
        }


    private fun CounterRequest.isIdValid() = id.isNotBlank()

    companion object {
        private const val MIN_TITLE_SIZE = 2
    }
}