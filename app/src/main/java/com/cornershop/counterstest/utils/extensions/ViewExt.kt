package com.cornershop.counterstest.utils.extensions

import androidx.appcompat.widget.SearchView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

const val SEARCH_DEBOUNCE_TIMEOUT = 500L

fun SearchView.observeTextChange(): Observable<String> {
    val searchTextObservable = PublishSubject.create<String>()
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean = true
        override fun onQueryTextChange(newText: String?): Boolean {
            val query = newText.orEmpty()
            searchTextObservable.onNext(query)
            return true
        }
    })
    return searchTextObservable.debounce(SEARCH_DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
}