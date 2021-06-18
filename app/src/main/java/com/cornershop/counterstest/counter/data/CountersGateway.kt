package com.cornershop.counterstest.counter.data

import io.reactivex.Single
import retrofit2.http.*

interface CountersGateway {

    @GET("v1/counters")
    fun getCounters(): Single<List<CounterRaw>>

    @POST("v1/counter")
    fun addCounter(@Body request: CounterRequestAdd): Single<List<CounterRaw>>

    @HTTP(method = "DELETE", path = "v1/counter", hasBody = true)
    fun deleteCounter(@Body request: CounterRequest): Single<List<CounterRaw>>

    @POST("v1/counter/inc")
    fun incrementCounter(@Body request: CounterRequest): Single<List<CounterRaw>>

    @POST("v1/counter/dec")
    fun decrementCounter(@Body request: CounterRequest): Single<List<CounterRaw>>

}