package com.cornershop.counterstest.counter.presentation.entries

import android.view.View
import androidx.core.view.isVisible
import com.cornershop.counterstest.R
import com.cornershop.counterstest.counter.model.Counter
import com.cornershop.counterstest.databinding.CountersCounterItemBinding
import com.xwray.groupie.viewbinding.BindableItem

class CounterEntry(
    private val counter: Counter,
    private val handler: CounterEntryHandler
) : BindableItem<CountersCounterItemBinding>() {

    override fun getLayout(): Int = R.layout.counters_counter_item
    override fun initializeViewBinding(view: View) = CountersCounterItemBinding.bind(view)

    override fun bind(viewBinding: CountersCounterItemBinding, position: Int) = with(viewBinding) {
        root.isSelected = counter.isSelected
        counterTitle.text = counter.title
        counterCount.text = counter.count.toString()
        counterControls.isVisible = counter.isSelected.not()
        counterCheckedIcon.isVisible = counter.isSelected
        counterInc.setOnClickListener { handler.onCounterIncrement(counter.id) }
        counterDec.apply {
            isEnabled = counter.hasCount
            setOnClickListener { this@CounterEntry.handler.onCounterDecrement(counter.id) }
        }
        root.setOnLongClickListener {
            counter.toggleSelected()
            handler.onCounterSelected()
            true
        }
    }
}