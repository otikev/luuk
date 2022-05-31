package com.elmenture.luuk.ui.main.accountmanagement.orderhistory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.base.repositories.RemoteRepository
import com.elmenture.luuk.repositories.AccountManagementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderHistoryViewModel : ViewModel() {
    val ordersApiState = MutableLiveData<BaseApiState>()
    val ordersItemsApiState = MutableLiveData<BaseApiState>()

    fun fetchAllOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            ordersApiState.postValue(AccountManagementRepository.fetchAllOrders())
        }
    }

    fun fetchOrdersItems(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            ordersItemsApiState.postValue(RemoteRepository.fetchOrderItems(id))
        }
    }
}