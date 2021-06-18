package com.cornershop.counterstest.counter.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CounterRequest(
    @SerialName("id") val id: String
)