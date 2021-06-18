package com.cornershop.counterstest.presentation.welcome

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cornershop.counterstest.di.CountersService
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val countersService: CountersService
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val response = MutableLiveData<Unit>()
    val exeption = MutableLiveData<Throwable>()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun getCounters() {
        countersService
            .getCounters()
            .performOnBack()
            .subscribe({
                response.postValue(Unit)
            }, { error ->
                Timber.e(error)
                exeption.postValue(error)
            }).addToComposite(compositeDisposable)
    }
}


fun Completable.performOnBack(): Completable {
    return this.subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
}

fun Completable.observeOnIo(): Completable {
    return this.observeOn(Schedulers.io())
}

fun Disposable.addToComposite(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}