package com.cornershop.counterstest.counter.presentation

import com.cornershop.counterstest.counter.model.Counter
import com.cornershop.counterstest.counter.presentation.entries.CounterEmptyEntry
import com.cornershop.counterstest.counter.presentation.entries.CounterEntry
import com.cornershop.counterstest.counter.presentation.entries.CounterEntryHandler
import com.cornershop.counterstest.counter.presentation.entries.CounterHeaderEntry
import com.xwray.groupie.Group

data class CountersPresentation(val entries: List<Group>) {

    companion object {
        operator fun invoke(
            counters: List<Counter>,
            handler: CounterEntryHandler
        ): CountersPresentation {
            val entries = mutableListOf<Group>()
            if (counters.isEmpty()) {
                entries.add(CounterEmptyEntry())
            } else {
                val totalItems = counters.size
                val totalTimes = counters.sumBy { counter -> counter.count }
                val counterHeader = CounterHeaderEntry(totalItems, totalTimes)
                val counterEntries = counters.map { counter -> CounterEntry(counter, handler) }
                entries.add(counterHeader)
                entries.addAll(counterEntries)
            }
            return CountersPresentation(entries)
        }
    }
}