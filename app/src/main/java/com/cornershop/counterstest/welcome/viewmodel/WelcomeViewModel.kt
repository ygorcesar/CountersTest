package com.cornershop.counterstest.welcome.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cornershop.counterstest.utils.data.StateMachineEvent
import com.cornershop.counterstest.utils.extensions.addToComposite
import com.cornershop.counterstest.utils.extensions.subscribeOnViewModel
import com.cornershop.counterstest.welcome.data.WelcomeService
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val infrastructure: WelcomeService
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val _isFirstTimeState = MutableLiveData<StateMachineEvent<Boolean>>()
    val isFirstTimeState: LiveData<StateMachineEvent<Boolean>> get() = _isFirstTimeState

    fun checkIsFirstTime() {
        infrastructure.isFirstTime()
            .subscribeOnViewModel(_isFirstTimeState)
            .addToComposite(compositeDisposable)
    }
}