package com.cornershop.counterstest.counter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cornershop.counterstest.counter.domain.CountersInteractor
import com.cornershop.counterstest.counter.model.Counter
import com.cornershop.counterstest.utils.StateMachineEvent
import com.cornershop.counterstest.utils.observeOnBack
import com.cornershop.counterstest.utils.subscribe
import com.cornershop.counterstest.utils.addToComposite
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class CountersViewModel @Inject constructor(
    private val interactor: CountersInteractor
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val _responseState = MutableLiveData<StateMachineEvent<List<Counter>>>()
    val responseState: LiveData<StateMachineEvent<List<Counter>>> get() = _responseState

    fun getCounters(fetchFromRemote: Boolean = true) {
        interactor.getCounters(fetchFromRemote)
            .observeOnBack()
            .subscribe(_responseState)
            .addToComposite(compositeDisposable)
    }
}