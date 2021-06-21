package com.cornershop.counterstest.counter.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cornershop.counterstest.R
import com.cornershop.counterstest.counter.presentation.entries.CreateCounterExamplesEntry
import com.cornershop.counterstest.databinding.CreateCounterExamplesActivityBinding
import com.cornershop.counterstest.utils.extensions.viewBinding
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCounterExamplesActivity : AppCompatActivity() {

    private val binding by viewBinding(CreateCounterExamplesActivityBinding::inflate)
    private val groupAdapter by lazy { GroupieAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupViews()
        setupExamples()
    }

    private fun setupViews() = with(binding) {
        toolbar.setNavigationOnClickListener { finish() }
        recyclerView.adapter = groupAdapter
    }

    private fun setupExamples() {
        val entries = mutableListOf<Pair<Int, Array<String>>>().apply {
            add(R.string.drinks to resources.getStringArray(R.array.drinks_array))
            add(R.string.food to resources.getStringArray(R.array.food_array))
            add(R.string.misc to resources.getStringArray(R.array.misc_array))
        }.map { (title, examples) ->
            CreateCounterExamplesEntry(title, examples, ::onExampleClicked)
        }
        groupAdapter.addAll(entries)
    }

    private fun onExampleClicked(example: String) {
        val intent = Intent().apply {
            putExtra(CreateCounterActivity.CREATE_COUNTER_EXAMPLE_KEY, example)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    companion object {

        fun newInstance(context: Context) = Intent(
            context,
            CreateCounterExamplesActivity::class.java
        )
    }
}