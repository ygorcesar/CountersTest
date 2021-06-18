package com.cornershop.counterstest.utils.extensions

import androidx.lifecycle.MutableLiveData
import com.cornershop.counterstest.utils.data.StateMachineEvent
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

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
            Timber.e(error)
            onError(error)
            responseState.postValue(StateMachineEvent.Failure(error))
        })
}

fun Disposable.addToComposite(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}
