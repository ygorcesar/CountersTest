package com.cornershop.counterstest.utils.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.cornershop.counterstest.utils.extensions.performOnBack
import io.reactivex.Single

/**
 * Injectable class which handles device network connection.
 */
class NetworkHandler(private val context: Context) {

    private val Context.connectivityManager: ConnectivityManager
        get() = (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)

    val isConnected: Boolean
        get() {
            val connectivityManager = context.connectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
}

fun <RAW, MODEL> NetworkHandler.request(
    body: () -> Single<RAW>,
    mapper: (RAW) -> MODEL
): Single<MODEL> = when (isConnected) {
    false -> Single.error(NetworkError)
    true -> body().performOnBack()
        .map { raw -> mapper(raw) }
}