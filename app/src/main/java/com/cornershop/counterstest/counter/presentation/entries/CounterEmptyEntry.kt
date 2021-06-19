package com.cornershop.counterstest.counter.presentation.entries

import android.view.View
import androidx.core.view.isVisible
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.CountersEmptyItemBinding
import com.xwray.groupie.viewbinding.BindableItem

class CounterEmptyEntry(
    private val isSearchingByQuery: Boolean = false
) : BindableItem<CountersEmptyItemBinding>() {

    override fun getLayout(): Int = R.layout.counters_empty_item
    override fun initializeViewBinding(view: View) = CountersEmptyItemBinding.bind(view)
    override fun bind(viewBinding: CountersEmptyItemBinding, position: Int) = with(viewBinding) {
        val titleResId = when (isSearchingByQuery) {
            true -> R.string.no_results
            false -> R.string.no_counters
        }
        title.setText(titleResId)
        description.isVisible = isSearchingByQuery.not()
    }
}