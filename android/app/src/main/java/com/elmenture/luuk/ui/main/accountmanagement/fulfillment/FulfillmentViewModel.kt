package com.elmenture.luuk.ui.main.accountmanagement.fulfillment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.base.repositories.RemoteRepository
import com.elmenture.luuk.repositories.AccountManagementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import models.Order
import models.OrderStateUpdate

class FulfillmentViewModel : ViewModel() {
    val ordersApiState = MutableLiveData<BaseApiState>()
    val ordersItemsApiState = MutableLiveData<BaseApiState>()
    val updateOrderStateApiState = MutableLiveData<BaseApiState>()

    fun fetchOrders(state:String) {
        viewModelScope.launch(Dispatchers.IO) {
            ordersApiState.postValue(AccountManagementRepository.fetchOrdersByState(state))
        }
    }

    fun fetchOrderItems(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            ordersItemsApiState.postValue(RemoteRepository.fetchOrderItems(id))
        }
    }

    fun updateOrderState(stateUpdate: OrderStateUpdate) {
        viewModelScope.launch(Dispatchers.IO) {
            updateOrderStateApiState.postValue(RemoteRepository.updateOrderState(stateUpdate))
        }
    }
}