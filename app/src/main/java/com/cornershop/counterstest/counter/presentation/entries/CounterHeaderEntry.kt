package com.cornershop.counterstest.counter.presentation.entries

import android.view.View
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.CountersHeaderItemBinding
import com.cornershop.counterstest.utils.extensions.getString
import com.xwray.groupie.viewbinding.BindableItem

class CounterHeaderEntry(
    private val itemsAmount: Int,
    private val totalTimesAmount: Int
) : BindableItem<CountersHeaderItemBinding>() {

    override fun getLayout(): Int = R.layout.counters_header_item
    override fun initializeViewBinding(view: View) = CountersHeaderItemBinding.bind(view)

    override fun bind(viewBinding: CountersHeaderItemBinding, position: Int) = with(viewBinding) {
        counterHeaderItemsAmount.text = getString(R.string.n_items, itemsAmount)
        counterHeaderTimes.text = getString(R.string.n_times, totalTimesAmount)
    }
}