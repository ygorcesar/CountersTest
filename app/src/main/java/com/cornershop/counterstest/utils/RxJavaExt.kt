package com.cornershop.counterstest.utils

import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

fun <T> Single<T>.performOnBack(): Single<T> {
    return this.subscribeOn(Schedulers.io())
}

fun <T> Single<T>.observeOnBack(): Single<T> {
    return this.observeOn(Schedulers.io())
}

fun <T> Single<T>.subscribe(
    responseState: MutableLiveData<StateMachineEvent<T>>,
    onSuccess: (T) -> Unit = {},
    onError: (Throwable) -> Unit = {}
): Disposable {
    return doOnSubscribe { responseState.postValue(StateMachineEvent.Start) }
        .subscribe({ response ->
            onSuccess(response)
            responseState.postValue(StateMachineEvent.Success(response))
        }, { error ->
            onError(error)
            responseState.postValue(StateMachineEvent.Failure(error))
        })
}

fun Disposable.addToComposite(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}
