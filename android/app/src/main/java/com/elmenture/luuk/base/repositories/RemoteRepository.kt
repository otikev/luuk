package com.elmenture.luuk.base.repositories

import androidx.lifecycle.MutableLiveData
import com.elmenture.luuk.base.BaseApiState
import models.OrderStateUpdate
import models.Action
import models.ActualMeasurements
import models.Item
import models.UpdateUserDetailsRequest
import network.RestClient
import network.interceptors.ConnectivityInterceptor
import network.service.EndPoints
import retrofit2.Call
import utils.CannedSearch
import utils.LogUtils
import utils.NetUtils

object RemoteRepository {
    val logUtils = LogUtils(javaClass)
    val blockUserInteraction = MutableLiveData(false)

    private fun processRequest(call: Call<*>, blockUi: Boolean = true): BaseApiState {
        var baseApiState: BaseApiState
        if (blockUi)
            this.blockUserInteraction.postValue(true)
        try {
            val response = call.execute()
            if (response.isSuccessful) {

                BaseApiState.SUCCESS_STATE.setData(response.body())
                BaseApiState.SUCCESS_STATE.responseCode = response.code()
                baseApiState = BaseApiState.SUCCESS_STATE
                logUtils.i("Success")
            } else {
                BaseApiState.ERROR_STATE.responseCode = response.code()
                BaseApiState.ERROR_STATE.errorMessage = response.errorBody()?.string()
                baseApiState = BaseApiState.ERROR_STATE
                logUtils.w("Api Error: %d".format(BaseApiState.ERROR_STATE.responseCode))

            }
        } catch (e: Exception) {
            if (e is ConnectivityInterceptor.NoConnectivityException) {
                BaseApiState.ERROR_STATE.responseCode = NetUtils.CONNECTIVITY_ERROR_CODE
            } else {
                BaseApiState.ERROR_STATE.responseCode = NetUtils.NETWORK_ERROR_CODE
            }
            logUtils.w("Connectivity Error: %d".format(BaseApiState.ERROR_STATE.responseCode))
            BaseApiState.ERROR_STATE.errorMessage = e.message
            baseApiState = BaseApiState.ERROR_STATE

        }
        if (blockUi)
            this.blockUserInteraction.postValue(false)
        return baseApiState
    }

    fun doPostUserBodyMeasurements(request: ActualMeasurements): BaseApiState {
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
            RestClient.serviceWithUserAuthentication(EndPoints::class.java)
                .updateUserDetails(request)
        return processRequest(call)
    }

    fun fetchAllItems(): BaseApiState {
        val call = RestClient.serviceWithUserAuthentication(EndPoints::class.java).fetchAllItems()
        return processRequest(call)
    }

    fun fetchItemsQueue(filter: Boolean): BaseApiState {
        val call =
            RestClient.serviceWithUserAuthentication(EndPoints::class.java).fetchItemsQueue(filter)
        return processRequest(call, blockUi = false)
    }

    fun updateItem(request: Item): BaseApiState {
        val call =
            RestClient.serviceWithUserAuthentication(EndPoints::class.java).updateItem(request)
        return processRequest(call)
    }

    fun logUserActions(request: Action): BaseApiState {
        val call =
            RestClient.serviceWithUserAuthentication(EndPoints::class.java).logUserActions(request)
        return processRequest(call, blockUi = false)
    }

    fun validateCartItems(cartItems: ArrayList<Long>): BaseApiState {
        val call = RestClient.serviceWithUserAuthentication(EndPoints::class.java)
            .validateCartItems(cartItems)
        return processRequest(call)
    }

    fun confirmOrder(merchantRequestID: String): BaseApiState {
        val call = RestClient.serviceWithUserAuthentication(EndPoints::class.java)
            .confirmOrder(merchantRequestID)
        return processRequest(call)
    }

    fun fetchAllOrders(): BaseApiState {
        val call = RestClient.serviceWithUserAuthentication(EndPoints::class.java).fetchAllOrders()
        return processRequest(call)
    }

    fun fetchOrdersByState(state: String): BaseApiState {
        val call = RestClient.serviceWithUserAuthentication(EndPoints::class.java)
            .fetchOrdersByState(state)
        return processRequest(call)
    }

    fun fetchUserDetails(): BaseApiState {
        val call = RestClient.serviceWithUserAuthentication(EndPoints::class.java).userDetails()
        return processRequest(call, blockUi = false)
    }

    fun fetchItemsByQuery(query: String): BaseApiState {
        val call = RestClient.serviceWithUserAuthentication(EndPoints::class.java)
            .fetchItemsWithQuery(query)
        return processRequest(call, blockUi = true)
    }

    fun fetchOrderItems(id: Int): BaseApiState {
        val call =
            RestClient.serviceWithUserAuthentication(EndPoints::class.java).fetchOrderItems(id)
        return processRequest(call, blockUi = true)
    }

    fun updateOrderState(stateUpdate: OrderStateUpdate): BaseApiState {
        val call =
            RestClient.serviceWithUserAuthentication(EndPoints::class.java).updateOrderState(stateUpdate)
        return processRequest(call, blockUi = true)
    }

    fun fetchCannedItems(query: CannedSearch): BaseApiState {
        val call = RestClient.serviceWithUserAuthentication(EndPoints::class.java)
            .fetchCannedItems(query.toString())
        return processRequest(call, blockUi = true)
    }
}