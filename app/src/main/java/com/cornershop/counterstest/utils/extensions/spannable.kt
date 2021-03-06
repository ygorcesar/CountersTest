package com.cornershop.counterstest.utils.extensions

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan

operator fun SpannableString.plus(given: SpannableString) =
    SpannableString(TextUtils.concat(this, given))

operator fun SpannableString.plus(given: String) =
    SpannableString(TextUtils.concat(this, given))

fun normal(given: CharSequence) =
    span(given, StyleSpan(Typeface.NORMAL))

fun bold(given: CharSequence) =
    span(given, StyleSpan(Typeface.BOLD))

fun underline(given: CharSequence) = span(given, UnderlineSpan())

private fun span(given: CharSequence, span: Any): SpannableString {

    val spannable =
        if (given is String) SpannableString(given)
        else given as? SpannableString ?: throw CannotBeSpanned

    return spannable.apply {
        setSpan(span, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}

object CannotBeSpanned : IllegalArgumentException(
    "Cannot apply span. Target should be String or SpannableString instance"
)