package com.cornershop.counterstest.utils.extensions

import android.view.LayoutInflater
import android.view.View
import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding

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

fun <T : Any, L : LiveData<T>> ComponentActivity.observe(liveData: L, action: (T) -> Unit) =
    liveData.observe(this, Observer(action))