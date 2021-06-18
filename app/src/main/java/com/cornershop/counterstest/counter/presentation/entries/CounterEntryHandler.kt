package com.cornershop.counterstest.counter.presentation.entries

interface CounterEntryHandler {

    fun onCounterSelected()
    fun onCounterIncrement(counterId: String)
    fun onCounterDecrement(counterId: String)
}