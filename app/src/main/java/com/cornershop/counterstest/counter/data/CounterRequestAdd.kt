package com.cornershop.counterstest.counter.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CounterRequestAdd(
    @SerialName("title") val title: String
)