package com.cornershop.counterstest.counter.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.cornershop.counterstest.R
import com.cornershop.counterstest.counter.domain.CounterBusiness
import com.cornershop.counterstest.counter.model.Counter
import com.cornershop.counterstest.counter.viewmodel.CreateCounterViewModel
import com.cornershop.counterstest.databinding.CreateCounterActivityBinding
import com.cornershop.counterstest.utils.data.NetworkError
import com.cornershop.counterstest.utils.data.StateMachineEvent
import com.cornershop.counterstest.utils.extensions.hideKeyboard
import com.cornershop.counterstest.utils.extensions.observe
import com.cornershop.counterstest.utils.extensions.snackBar
import com.cornershop.counterstest.utils.extensions.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCounterActivity : AppCompatActivity() {

    private val binding by viewBinding(CreateCounterActivityBinding::inflate)
    private val viewModel by viewModels<CreateCounterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupViews()
        setupObservers()
    }

    private fun setupViews() = with(binding) {
        save.setOnClickListener { saveCounter() }
        close.setOnClickListener { finish() }
        seeSuggestions.setOnClickListener { navigateToSeeSuggestions() }
    }

    private fun setupObservers() = with(viewModel) {
        observe(counterState, ::onSaveCounterStateChanged)
    }

    private fun onSaveCounterStateChanged(event: StateMachineEvent<List<Counter>>) = when (event) {
        StateMachineEvent.Start -> loading(true)
        is StateMachineEvent.Success -> onCounterAddedWithSuccess()
        is StateMachineEvent.Failure -> showError(event.exception)
    }

    private fun saveCounter() {
        hideKeyboard()
        val title = binding.counterTitleInput.editText?.text?.toString().orEmpty()
        viewModel.createCounter(title)
    }

    private fun onCounterAddedWithSuccess() {
        snackBar(R.string.added_counter_with_success, onDismissed = {
            loading(false)
            setResult(RESULT_OK)
            finish()
        })
    }

    private fun loading(isLoading: Boolean) = with(binding) {
        progressLoading.isVisible = isLoading
        save.isVisible = isLoading.not()
    }

    private fun showError(error: Throwable) {
        val (title, message) = when (error) {
            NetworkError -> R.string.error_creating_counter_title to R.string.connection_error_description
            CounterBusiness.InvalidEmptyTitle -> R.string.error_creating_counter_title to R.string.error_invalid_counter_empty_title
            CounterBusiness.InvalidTitleSize -> R.string.error_creating_counter_title to R.string.error_invalid_counter_title_size
            else -> R.string.error_default_title to R.string.error_default_description
        }

        loading(false)
        MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.ok) { _, _ -> }
            .create()
            .show()
    }

    private fun navigateToSeeSuggestions() {
        val intent = CreateCounterExamplesActivity.newInstance(this)
        resultLauncher.launch(intent)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getStringExtra(CREATE_COUNTER_EXAMPLE_KEY)?.let { example ->
                    binding.counterTitleInput.editText?.setText(example)
                }
            }
        }

    companion object {
        const val CREATE_COUNTER_EXAMPLE_KEY = "create_counter_example_key"

        fun newInstance(context: Context) = Intent(context, CreateCounterActivity::class.java)
    }
}