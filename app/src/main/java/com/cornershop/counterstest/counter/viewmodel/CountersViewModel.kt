package com.cornershop.counterstest.counter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cornershop.counterstest.counter.domain.CountersInteractor
import com.cornershop.counterstest.counter.model.Counter
import com.cornershop.counterstest.utils.data.StateMachineEvent
import com.cornershop.counterstest.utils.extensions.subscribeOnViewModel
import com.cornershop.counterstest.utils.extensions.addToComposite
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class CountersViewModel @Inject constructor(
    private val interactor: CountersInteractor
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val _countersState = MutableLiveData<StateMachineEvent<List<Counter>>>()
    val countersState: LiveData<StateMachineEvent<List<Counter>>> get() = _countersState

    fun getCounters(fetchFromRemote: Boolean = true) {
        interactor.getCounters(fetchFromRemote)
            .subscribeOnViewModel(_countersState)
            .addToComposite(compositeDisposable)
    }
}