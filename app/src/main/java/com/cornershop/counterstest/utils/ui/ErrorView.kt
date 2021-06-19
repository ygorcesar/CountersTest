package com.cornershop.counterstest.utils.ui

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.cornershop.counterstest.R
import com.cornershop.counterstest.utils.data.NetworkError
import com.cornershop.counterstest.utils.extensions.getString
import kotlinx.android.synthetic.main.error_view.view.*

class ErrorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        isClickable = true
        isFocusable = true
        setBackgroundColor(context.getColor(R.color.white))
        View.inflate(context, R.layout.error_view, this)
    }

    fun showError(throwable: Throwable?, onButtonClick: () -> Unit = {}) {
        setActionButtonClick(onButtonClick)
        if (throwable is NetworkError) showConnectionError()
        else showError()
    }

    private fun showError(
        title: Int = R.string.error_default_title,
        description: Int = R.string.error_default_description
    ) {
        errorViewTitle.text = getString(title)
        errorViewDescription.text = getString(description)
        showView()
    }

    private fun showConnectionError() {
        errorViewTitle.text = getString(R.string.error_load_counters_title)
        errorViewDescription.text = getString(R.string.connection_error_description)
        showView()
    }

    private fun setActionButtonClick(onButtonClick: () -> Unit) {
        errorViewButton.setOnClickListener {
            hideError()
            onButtonClick()
        }
    }

    private fun showView() {
        isVisible = true
    }

    private fun hideError() {
        isVisible = false
    }
}
