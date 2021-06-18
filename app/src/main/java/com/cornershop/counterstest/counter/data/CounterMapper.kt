package com.cornershop.counterstest.counter.data

import com.cornershop.counterstest.counter.model.Counter
import javax.inject.Inject

class CounterMapper @Inject constructor() {

    fun mapToModel(counters: List<CounterRaw>): List<Counter> = counters.map { counter ->
        Counter(
            id = counter.id.orEmpty(),
            title = counter.title.orEmpty(),
            count = counter.count ?: 0
        )
    }.filter { counter ->
        counter.id.isNotBlank() && counter.title.isNotBlank()
    }
}