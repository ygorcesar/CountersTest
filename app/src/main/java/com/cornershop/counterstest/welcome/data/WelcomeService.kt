package com.cornershop.counterstest.welcome.data

import io.reactivex.Single

interface WelcomeService {

    fun isFirstTime(): Single<Boolean>
}