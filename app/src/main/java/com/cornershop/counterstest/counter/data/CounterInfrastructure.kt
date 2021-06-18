package com.cornershop.counterstest.counter.data

import com.cornershop.counterstest.counter.model.Counter
import com.cornershop.counterstest.utils.data.DataStore
import com.cornershop.counterstest.utils.data.NetworkHandler
import com.cornershop.counterstest.utils.data.request
import io.reactivex.Single
import javax.inject.Inject

class CounterInfrastructure @Inject constructor(
    private val api: CountersGateway,
    private val counterMapper: CounterMapper,
    private val networkHandler: NetworkHandler,
    private val dataStore: DataStore
) : CounterService {

    override fun getCounters(fetchFromRemote: Boolean): Single<List<Counter>> =
        when (fetchFromRemote) {
            true -> networkHandler.request(api::getCounters, counterMapper::mapToModel)
                .flatMap(::updateLocalCounters)
            false -> Single.create { emitter ->
                val counters =
                    dataStore.get<List<Counter>>(COUNTERS_KEY, listOf())
                emitter.onSuccess(counters)
            }
        }

    override fun addCounter(request: CounterRequestAdd): Single<List<Counter>> =
        networkHandler.request({ api.addCounter(request) }, counterMapper::mapToModel)
            .flatMap(::updateLocalCounters)

    override fun deleteCounter(request: CounterRequest): Single<List<Counter>> =
        networkHandler.request({ api.deleteCounter(request) }, counterMapper::mapToModel)
            .flatMap(::updateLocalCounters)

    override fun incrementCounter(request: CounterRequest): Single<List<Counter>> =
        networkHandler.request({ api.incrementCounter(request) }, counterMapper::mapToModel)
            .flatMap(::updateLocalCounters)

    override fun decrementCounter(request: CounterRequest): Single<List<Counter>> =
        networkHandler.request({ api.decrementCounter(request) }, counterMapper::mapToModel)
            .flatMap(::updateLocalCounters)

    override fun updateLocalCounters(
        counters: List<Counter>
    ) = Single.create<List<Counter>> { emitter ->
        dataStore.put(COUNTERS_KEY, counters)
        emitter.onSuccess(counters)
    }

    companion object {
        private const val COUNTERS_KEY = "counter"
    }
}