package com.elmenture.luuk.ui.main.accountmanagement.orderhistory

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.base.repositories.LocalRepository
import com.elmenture.luuk.repositories.AccountManagementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import models.ActualMeasurements

class OrderHistoryViewModel : ViewModel() {
    val ordersApiState = MutableLiveData<BaseApiState>()

    fun fetchAllOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            ordersApiState.postValue(AccountManagementRepository.fetchAllOrders())
        }
    }
}