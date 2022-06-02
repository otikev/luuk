package com.elmenture.luuk.ui.main.accountmanagement.fulfillment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.base.repositories.RemoteRepository
import com.elmenture.luuk.repositories.AccountManagementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FulfillmentViewModel : ViewModel() {
    val ordersApiState = MutableLiveData<BaseApiState>()

    fun fetchOrders(state:String) {
        viewModelScope.launch(Dispatchers.IO) {
            ordersApiState.postValue(AccountManagementRepository.fetchOrdersByState(state))
        }
    }

}