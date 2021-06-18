package com.cornershop.counterstest.counter.data

import com.cornershop.counterstest.counter.model.Counter
import com.cornershop.counterstest.utils.NetworkHandler
import com.cornershop.counterstest.utils.request
import io.reactivex.Single
import javax.inject.Inject

class CounterInfrastructure @Inject constructor(
    private val api: CountersGateway,
    private val counterMapper: CounterMapper,
    private val networkHandler: NetworkHandler
) : CounterService {

    override fun getCounters(): Single<List<Counter>> =
        networkHandler.request(api::getCounters, counterMapper::mapToModel)

    override fun addCounter(request: CounterRequestAdd): Single<List<Counter>> =
        networkHandler.request({ api.addCounter(request) }, counterMapper::mapToModel)

    override fun deleteCounter(request: CounterRequest): Single<List<Counter>> =
        networkHandler.request({ api.deleteCounter(request) }, counterMapper::mapToModel)

    override fun incrementCounter(request: CounterRequest): Single<List<Counter>> =
        networkHandler.request({ api.incrementCounter(request) }, counterMapper::mapToModel)

    override fun decrementCounter(request: CounterRequest): Single<List<Counter>> =
        networkHandler.request({ api.decrementCounter(request) }, counterMapper::mapToModel)
}