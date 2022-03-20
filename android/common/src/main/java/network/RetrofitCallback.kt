package network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by kevin on 26/10/17.
 *
 *
 * This callback eliminates the need for boiler-plate code to
 * differentiate between success and failed responses
 */
abstract class RetrofitCallback<T> : Callback<T> {
    /**
     * Invoked for a received HTTP response.
     *
     *
     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
     * Call [Response.isSuccessful] to determine if the response indicates success.
     *
     * @param call
     * @param response
     */
    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            onRequestSuccess(response.body(), response.code())
        } else {
            onRequestFailure(call, response)
        }
    }

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     *
     * @param call
     * @param t
     */
    override fun onFailure(call: Call<T>, t: Throwable) {
        onNetworkError()
    }

    abstract fun onRequestSuccess(response: T?, responseCode: Int)
    abstract fun onRequestFailure(call: Call<T>?, response: Response<T>?)
    abstract fun onNetworkError()
}