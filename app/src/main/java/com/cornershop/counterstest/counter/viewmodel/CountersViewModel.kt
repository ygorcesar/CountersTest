package com.cornershop.counterstest.counter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cornershop.counterstest.counter.domain.CountersInteractor
import com.cornershop.counterstest.counter.model.Counter
import com.cornershop.counterstest.utils.data.StateMachineEvent
import com.cornershop.counterstest.utils.extensions.addToComposite
import com.cornershop.counterstest.utils.extensions.subscribeOnViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class CountersViewModel @Inject constructor(
    private val interactor: CountersInteractor
) : ViewModel() {

    private var counters: List<Counter> = emptyList()
    private val compositeDisposable = CompositeDisposable()
    private val _counterNotFound = MutableLiveData<Boolean>()
    private val _countersState = MutableLiveData<StateMachineEvent<List<Counter>>>()

    val counterNotFound: LiveData<Boolean> get() = _counterNotFound
    val countersState: LiveData<StateMachineEvent<List<Counter>>> get() = _countersState

    fun getCounters(fetchFromRemote: Boolean = true) {
        interactor.getCounters(fetchFromRemote)
            .doOnSuccess { counters -> this.counters = counters }
            .subscribeOnViewModel(_countersState)
            .addToComposite(compositeDisposable)
    }

    fun filterByQuery(query: String) {
        val filteredCounters = counters.filter { counter ->
            counter.title.contains(query, ignoreCase = true) || query.isBlank()
        }
        if (filteredCounters.isEmpty() && counters.isNotEmpty()) {
            _counterNotFound.postValue(true)
        } else {
            _counterNotFound.postValue(false)
            _countersState.postValue(StateMachineEvent.Success(filteredCounters))
        }
    }
}