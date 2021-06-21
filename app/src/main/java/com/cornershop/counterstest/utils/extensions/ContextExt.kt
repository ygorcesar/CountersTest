package com.cornershop.counterstest.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar

inline fun <T : ViewBinding> ComponentActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) = lazy(LazyThreadSafetyMode.NONE) { bindingInflater.invoke(layoutInflater) }

fun View.getString(
    @StringRes stringResId: Int
) = this.context.getString(stringResId)

inline fun <reified T : ViewBinding> T.getString(
    @StringRes stringResId: Int
) = this.root.context.getString(stringResId)

fun <T : ViewBinding> T.getString(
    @StringRes stringResId: Int,
    vararg args: Any
) = this.root.context.getString(stringResId, *args)

fun <T : ViewBinding> T.getColor(
    @ColorRes colorResId: Int
) = ContextCompat.getColor(root.context, colorResId)

fun <T : Any, L : LiveData<T>> ComponentActivity.observe(liveData: L, action: (T) -> Unit) =
    liveData.observe(this, Observer(action))

fun Activity?.hideKeyboard() {
    this?.findViewById<View>(android.R.id.content)?.let {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

fun Activity.rootView(): View = findViewById(android.R.id.content)

fun Activity.snackBar(
    @StringRes stringResId: Int,
    duration: Int = Snackbar.LENGTH_SHORT,
    onDismissed: () -> Unit = {}
) = Snackbar.make(rootView(), stringResId, duration)
    .addCallback(object : Snackbar.Callback() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)
            onDismissed()
        }
    }).show()

fun Intent.clearBackStackFlags(): Intent {
    return apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}

fun textShareIntent(text: String, title: String? = null): Intent {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    return Intent.createChooser(sendIntent, title)
}