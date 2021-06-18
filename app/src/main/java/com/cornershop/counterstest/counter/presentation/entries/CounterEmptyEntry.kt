package com.cornershop.counterstest.counter.presentation.entries

import android.view.View
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.CountersEmptyItemBinding
import com.xwray.groupie.viewbinding.BindableItem

class CounterEmptyEntry : BindableItem<CountersEmptyItemBinding>() {

    override fun getLayout(): Int = R.layout.counters_empty_item
    override fun initializeViewBinding(view: View) = CountersEmptyItemBinding.bind(view)
    override fun bind(viewBinding: CountersEmptyItemBinding, position: Int) = Unit
}