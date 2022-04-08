package com.elmenture.luuk.base.repositories

import androidx.lifecycle.MutableLiveData
import com.elmenture.luuk.base.BaseApiState
import models.Item
import models.UpdateUserDetailsRequest
import models.UserMeasurements
import network.RestClient
import network.interceptors.ConnectivityInterceptor
import network.service.EndPoints
import retrofit2.Call
import utils.LogUtils
import utils.NetUtils

object RemoteRepository {
    val responseErrorCode = MutableLiveData<Int>()
    val blockUserInteraction = MutableLiveData(false)
    val TAG = "BaseRepository"

    private fun processRequest(call: Call<*>, blockUi:Boolean = true): BaseApiState {
        var viewState: BaseApiState
        if(blockUi)
            this.blockUserInteraction.postValue(true)
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
        if(blockUi)
            this.blockUserInteraction.postValue(false)
        return viewState
    }

    fun doPostUserBodyMeasurements(request: UserMeasurements): BaseApiState {
        val call = RestClient.serviceWithUserAuthentication(EndPoints::class.java)
            .postUserBodyMeasurements(request)
        return processRequest(call)
    }

    fun getUserBodyMeasurements(): BaseApiState {
        val call = RestClient.serviceWithUserAuthentication(EndPoints::class.java)
            .getUserBodyMeasurements()
        return processRequest(call)
    }

    fun signInWithGoogle(request: HashMap<String, String>): BaseApiState {
        val call = RestClient.service(EndPoints::class.java).googleSignIn(request)
        return processRequest(call)
    }

    fun signInWithFacebook(request: HashMap<String, String>): BaseApiState {
        val call = RestClient.service(EndPoints::class.java).facebookSignIn(request)
        return processRequest(call)
    }

    fun fetchItems(): BaseApiState {
        val call = RestClient.serviceWithUserAuthentication(EndPoints::class.java).fetchItems()
        return processRequest(call)
    }

    fun fetchItemsPaginated(page: Int, size: Int): BaseApiState {
        val call = RestClient.serviceWithUserAuthentication(EndPoints::class.java)
            .fetchItemsPaginated(page, size)
        return processRequest(call, false)
    }

    fun createNewItem(request: Item): BaseApiState {
        val call =
            RestClient.serviceWithUserAuthentication(EndPoints::class.java).createNewItem(request)
        return processRequest(call, blockUi = false)
    }

    fun updateUserDetails(request: UpdateUserDetailsRequest): BaseApiState {
        val call =
            RestClient.serviceWithUserAuthentication(EndPoints::class.java).updateUserDetails(request)
        return processRequest(call)
    }

}