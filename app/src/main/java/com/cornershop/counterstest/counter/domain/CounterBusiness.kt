package com.cornershop.counterstest.counter.domain

import com.cornershop.counterstest.utils.CountersException

sealed class CounterBusiness : CountersException() {

    object InvalidId : CounterBusiness()
    object InvalidEmptyTitle : CounterBusiness()
    object InvalidTitleSize : CounterBusiness()
}