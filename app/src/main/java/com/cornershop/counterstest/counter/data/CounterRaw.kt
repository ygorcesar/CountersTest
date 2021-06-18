package com.cornershop.counterstest.counter.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CounterRaw(
    @SerialName("id") val id: String?,
    @SerialName("title") val title: String?,
    @SerialName("count") val count: Int?
)