package com.cornershop.counterstest.counter.model

import kotlinx.serialization.Serializable

@Serializable
data class Counter(
    val id: String,
    val title: String,
    val count: Int,
    var isSelected: Boolean = false
) {
    val hasCount: Boolean
        get() = count > 0

    fun toggleSelected() {
        isSelected = isSelected.not()
    }
}