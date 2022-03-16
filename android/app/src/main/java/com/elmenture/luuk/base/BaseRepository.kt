package com.elmenture.luuk.base

import androidx.lifecycle.MutableLiveData
import com.luuk.common.network.service.EndPoints
import com.luuk.common.models.BodyMeasurements
import com.luuk.common.network.RestClient
import com.luuk.common.network.interceptors.ConnectivityInterceptor
import com.luuk.common.utils.LogUtils
import com.luuk.common.utils.NetUtils
import retrofit2.Call

object BaseRepository {
    val responseErrorCode = MutableLiveData<Int>()
    val blockUserInteraction = MutableLiveData(false)
    val TAG="BaseRepository"

    private fun processRequest(call: Call<*>): BaseApiState {
        var viewState: BaseApiState
        blockUserInteraction.postValue(true)
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                BaseApiState.SUCCESS_STATE.data = response.body() as Any
                viewState = BaseApiState.SUCCESS_STATE
                LogUtils.logI(TAG, "Success")
            } else {
                BaseApiState.ERROR_STATE.errorCode = response.code()
                BaseApiState.ERROR_STATE.errorMessage = response.errorBody()?.string()
                viewState = BaseApiState.ERROR_STATE
                LogUtils.logW(TAG, "Api Error: %d".format(BaseApiState.ERROR_STATE.errorCode))

            }
        } catch (e: Exception) {
            if (e is ConnectivityInterceptor.NoConnectivityException) {
                BaseApiState.ERROR_STATE.errorCode = NetUtils.CONNECTIVITY_ERROR_CODE
            } else {
                BaseApiState.ERROR_STATE.errorCode = NetUtils.NETWORK_ERROR_CODE
            }
            LogUtils.logW(TAG, "Connectivity Error: %d".format(BaseApiState.ERROR_STATE.errorCode))
            BaseApiState.ERROR_STATE.errorMessage = e.message
            viewState = BaseApiState.ERROR_STATE

        }
        viewState.errorCode?.let { responseErrorCode.postValue(it) }
        blockUserInteraction.postValue(false)
        return viewState
    }

    fun doPostUserBodyMeasurements(request: BodyMeasurements): BaseApiState {
        val call = RestClient.serviceWithUserAuthentication(EndPoints::class.java).postUserBodyMeasurements(request)
        return processRequest(call)
    }

}