package com.cornershop.counterstest.counter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cornershop.counterstest.counter.data.CounterRequestAdd
import com.cornershop.counterstest.counter.domain.CountersInteractor
import com.cornershop.counterstest.counter.model.Counter
import com.cornershop.counterstest.utils.data.StateMachineEvent
import com.cornershop.counterstest.utils.extensions.addToComposite
import com.cornershop.counterstest.utils.extensions.subscribeOnViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class CreateCounterViewModel @Inject constructor(
    private val interactor: CountersInteractor
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val _counterState = MutableLiveData<StateMachineEvent<List<Counter>>>()
    val counterState: LiveData<StateMachineEvent<List<Counter>>> get() = _counterState

    fun createCounter(title: String) {
        val request = CounterRequestAdd(title)
        interactor.addCounter(request)
            .subscribeOnViewModel(_counterState)
            .addToComposite(compositeDisposable)
    }
}