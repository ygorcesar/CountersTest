package com.cornershop.counterstest.counter.presentation.entries

import com.cornershop.counterstest.counter.model.Counter

interface CounterEntryHandler {

    fun onCounterSelected(counter: Counter)
    fun onCounterIncrement(counter: Counter)
    fun onCounterDecrement(counter: Counter)
}