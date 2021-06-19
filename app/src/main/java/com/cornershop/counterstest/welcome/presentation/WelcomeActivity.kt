package com.cornershop.counterstest.welcome.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cornershop.counterstest.counter.presentation.CountersActivity
import com.cornershop.counterstest.databinding.WelcomeActivityBinding
import com.cornershop.counterstest.utils.data.StateMachineEvent
import com.cornershop.counterstest.utils.extensions.observe
import com.cornershop.counterstest.utils.extensions.viewBinding
import com.cornershop.counterstest.welcome.viewmodel.WelcomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity() {

    private val binding by viewBinding(WelcomeActivityBinding::inflate)
    private val viewModel by viewModels<WelcomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupViews()
        setupObservers()
        loadInitialData()
    }

    private fun setupViews() = with(binding) {
        this.welcomeContent?.buttonStart?.setOnClickListener {
            navigateToHome()
        }
    }

    private fun setupObservers() = with(viewModel) {
        observe(isFirstTimeState, ::onIsFirstTimeStateChanged)
    }

    private fun loadInitialData() {
        viewModel.checkIsFirstTime()
    }

    private fun onIsFirstTimeStateChanged(event: StateMachineEvent<Boolean>) {
        if (event is StateMachineEvent.Success && !event.value) {
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        val intent = CountersActivity.newInstance(this)
        startActivity(intent)
    }
}