package com.cornershop.counterstest.counter.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.cornershop.counterstest.counter.model.Counter
import com.cornershop.counterstest.counter.presentation.entries.CounterEntryHandler
import com.cornershop.counterstest.counter.viewmodel.CountersViewModel
import com.cornershop.counterstest.databinding.CountersActivityBinding
import com.cornershop.counterstest.utils.data.StateMachineEvent
import com.cornershop.counterstest.utils.extensions.observe
import com.cornershop.counterstest.utils.extensions.viewBinding
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountersActivity : AppCompatActivity(), CounterEntryHandler {

    private val binding by viewBinding(CountersActivityBinding::inflate)
    private val viewModel by viewModels<CountersViewModel>()
    private val groupAdapter by lazy { GroupieAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupViews()
        setupObservers()
        getCounters()
    }

    private fun setupViews() = with(binding) {
        recyclerView.adapter = groupAdapter
    }

    private fun setupObservers() = with(viewModel) {
        observe(countersState, ::onCountersStateChanged)
    }

    private fun getCounters() {
        viewModel.getCounters()
    }

    private fun onCountersStateChanged(event: StateMachineEvent<List<Counter>>) = when (event) {
        StateMachineEvent.Start -> loading(true)
        is StateMachineEvent.Success -> showCounters(event.value)
        is StateMachineEvent.Failure -> showError(event.exception, ::getCounters)
    }

    private fun showCounters(counters: List<Counter>) {
        loading(false)
        groupAdapter.update(CountersPresentation(counters, this).entries)
    }

    private fun showError(error: Throwable, onRetry: () -> Unit) = with(binding) {
        loading(false)
        errorView.showError(error, onRetry)
    }

    private fun loading(isLoading: Boolean) = with(binding) {
        progressLoading.isVisible = isLoading
        recyclerView.isVisible = isLoading.not()
    }

    override fun onCounterDecrement(counterId: String) {
        // TODO
    }

    override fun onCounterIncrement(counterId: String) {
        // TODO
    }

    override fun onCounterSelected() {
        groupAdapter.notifyDataSetChanged()
    }

    companion object {

        fun newInstance(context: Context) = Intent(context, CountersActivity::class.java)
    }
}