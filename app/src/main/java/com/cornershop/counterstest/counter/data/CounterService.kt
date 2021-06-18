package com.cornershop.counterstest.counter.data

import com.cornershop.counterstest.counter.model.Counter
import io.reactivex.Single

interface CounterService {

    fun getCounters(): Single<List<Counter>>
    fun addCounter(request: CounterRequestAdd): Single<List<Counter>>
    fun deleteCounter(request: CounterRequest): Single<List<Counter>>
    fun incrementCounter(request: CounterRequest): Single<List<Counter>>
    fun decrementCounter(request: CounterRequest): Single<List<Counter>>
}