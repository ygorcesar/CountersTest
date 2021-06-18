package com.cornershop.counterstest.utils

sealed class StateMachineEvent<out T> {
    object Start : StateMachineEvent<Nothing>()
    data class Success<out T>(val value: T) : StateMachineEvent<T>()
    data class Failure(val exception: Throwable) : StateMachineEvent<Nothing>()
}