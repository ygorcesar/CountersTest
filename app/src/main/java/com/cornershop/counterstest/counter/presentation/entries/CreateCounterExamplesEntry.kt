package com.cornershop.counterstest.counter.presentation.entries

import android.view.View
import androidx.annotation.StringRes
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.CreateCounterExamplesItemBinding
import com.cornershop.counterstest.utils.extensions.getColor
import com.google.android.material.chip.Chip
import com.xwray.groupie.viewbinding.BindableItem

class CreateCounterExamplesEntry(
    @StringRes private val titleResId: Int,
    private val examples: Array<String>,
    private val onExampleClicked: (String) -> Unit
) : BindableItem<CreateCounterExamplesItemBinding>() {

    override fun getLayout(): Int = R.layout.create_counter_examples_item
    override fun initializeViewBinding(view: View) = CreateCounterExamplesItemBinding.bind(view)

    override fun bind(
        viewBinding: CreateCounterExamplesItemBinding,
        position: Int
    ) = with(viewBinding) {
        title.setText(titleResId)
        chipGroup.removeAllViews()
        examples.map { example ->
            Chip(root.context).apply {
                text = example
                setTextColor(getColor(R.color.black))
                setChipBackgroundColorResource(R.color.light_gray)
                setOnClickListener { onExampleClicked(example) }
            }
        }.forEach(chipGroup::addView)
    }
}