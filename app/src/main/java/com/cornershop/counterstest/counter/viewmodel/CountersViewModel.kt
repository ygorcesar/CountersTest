package com.cornershop.counterstest.counter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cornershop.counterstest.counter.data.CounterRequest
import com.cornershop.counterstest.counter.domain.CountersInteractor
import com.cornershop.counterstest.counter.model.Counter
import com.cornershop.counterstest.utils.data.NetworkError
import com.cornershop.counterstest.utils.data.StateMachineEvent
import com.cornershop.counterstest.utils.extensions.addToComposite
import com.cornershop.counterstest.utils.extensions.subscribeOnViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class CountersViewModel @Inject constructor(
    private val interactor: CountersInteractor
) : ViewModel() {

    private var counters: List<Counter> = emptyList()
    private val compositeDisposable = CompositeDisposable()
    private val _warnAboutConnection = MutableLiveData<Unit>()

    private val _counterNotFound = MutableLiveData<Boolean>()
    private val _countersSelected = MutableLiveData<MutableList<Counter>>()
    private val _countersState = MutableLiveData<StateMachineEvent<List<Counter>>>()
    private val _changeCounterCountState = MutableLiveData<StateMachineEvent<List<Counter>>>()
    private val _deleteCountersState = MutableLiveData<StateMachineEvent<List<Counter>>>()

    var counterToUpdate: Counter? = null
    val warnAboutConnection: LiveData<Unit> get() = _warnAboutConnection
    val counterNotFound: LiveData<Boolean> get() = _counterNotFound
    val countersSelected: LiveData<MutableList<Counter>> get() = _countersSelected
    val countersState: LiveData<StateMachineEvent<List<Counter>>> get() = _countersState
    val changeCounterCountState: LiveData<StateMachineEvent<List<Counter>>> get() = _changeCounterCountState
    val deleteCountersState: LiveData<StateMachineEvent<List<Counter>>> get() = _deleteCountersState

    fun getCounters(fetchFromRemote: Boolean = true) {
        interactor.getCounters(fetchFromRemote)
            .onErrorResumeNext { error ->
                if (error is NetworkError) {
                    interactor.getCounters(fetchFromRemote = false)
                        .flatMap { counters ->
                            if (counters.isEmpty()) Single.error(error)
                            else Single.just(counters)
                        }.doOnSuccess { counters ->
                            if (counters.isEmpty()) {
                                _countersState.postValue(StateMachineEvent.Failure(error))
                            } else _warnAboutConnection.postValue(Unit)
                        }
                } else Single.error(error)
            }
            .doOnSuccess { counters -> this.counters = counters }
            .subscribeOnViewModel(_countersState)
            .addToComposite(compositeDisposable)
    }

    fun incrementCounter(counter: Counter) {
        counterToUpdate = counter.copy(count = counter.count + 1)
        val request = CounterRequest(counter.id)
        interactor.incrementCounter(request)
            .doOnSuccess { counterToUpdate = null }
            .subscribeOnViewModel(_changeCounterCountState)
            .addToComposite(compositeDisposable)
    }

    fun decrementCounter(counter: Counter) {
        counterToUpdate = counter.copy(count = counter.count - 1)
        val request = CounterRequest(counter.id)
        interactor.decrementCounter(request)
            .doOnSuccess { counterToUpdate = null }
            .subscribeOnViewModel(_changeCounterCountState)
            .addToComposite(compositeDisposable)
    }

    fun deleteSelectedCounters() {
        val jobs = countersSelected.value?.map { counter ->
            val request = CounterRequest(counter.id)
            interactor.deleteCounter(request)
        } ?: return
        Single.merge(jobs)
            .doOnComplete { clearSelectedCounters() }
            .subscribeOnViewModel(_deleteCountersState)
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

    fun toggleSelectedCounter(counter: Counter) {
        val counters = _countersSelected.value ?: mutableListOf()
        if (counter.isSelected) counters.add(counter)
        else counters.remove(counter)
        _countersSelected.postValue(counters)
    }

    fun clearSelectedCounters() {
        _countersSelected.value?.forEach { it.isSelected = false }
        _countersSelected.postValue(mutableListOf())
    }
}