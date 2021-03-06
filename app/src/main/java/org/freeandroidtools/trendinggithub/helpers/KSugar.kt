package org.freeandroidtools.trendinggithub.helpers

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Prettify Call<T>.enqueue
 *
 * enqueue(
 * {},
 * {})
 *
 * instead of
 *
 * enqueue(object : Callback<T> {
 *     override fun onResponse(call: Call<SearchResult>?, response: Response<SearchResult>?) {}
 *     override fun onFailure(call: Call<SearchResult>?, t: Throwable?) {}
 * })
 */
fun <T> Call<T>.enqueue(success: (response: Response<T>) -> Unit,
                        failure: ((t: Throwable) -> Unit)? = null) {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>?, response: Response<T>) = success(response)
        override fun onFailure(call: Call<T>?, t: Throwable) {
            failure?.invoke(t)
        }
    })
}


/**
 * Prettify LiveData.observe calls
 *
 * observe(this){}
 *
 * instead of
 *
 * observe(this, Observer{})
 */
fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: (data: T?) -> Unit) {
    observe(owner, Observer { observer.invoke(it) })
}

fun <T> AppCompatActivity.handleError(error: Error<T>) {
    Toast.makeText(this, error.error.message, Toast.LENGTH_LONG).show()
}

fun <T> View.handleError(error: Error<T>) {
    Snackbar.make(this, error.error.message.toString(), Snackbar.LENGTH_SHORT).show()
}