package com.elmenture.luuk.ui.main.cart

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.base.repositories.LocalRepository
import com.elmenture.luuk.repositories.AccountManagementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import models.Spot
import models.StkConfirmationResponse

class CartViewModel : ViewModel() {

    var cartItemsLiveData = MediatorLiveData<MutableSet<Spot>>()
    var paymentApiState = MutableLiveData<BaseApiState>()
    var orderConfirmationApiState = MutableLiveData<BaseApiState>()
    var paymentStatus = MutableLiveData<PaymentStatus>(PaymentStatus.EmptyState)

    init {
        cartItemsLiveData.addSource(LocalRepository.swipeRecords.likes) {
            cartItemsLiveData.setValue(it)
        }
    }

    fun updateCart(cart: MutableSet<Spot>?) {
        LocalRepository.swipeRecords.likes.postValue(cart)
    }

    fun getCartItems(): ArrayList<Long> {
        val itemIds = arrayListOf<Long>()
        for (spot: Spot in cartItemsLiveData.value!!) {
            itemIds.add(spot.itemId)
        }
        return itemIds
    }

    fun validateCartItems() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = AccountManagementRepository.validateCartItems(getCartItems())
            withContext(Dispatchers.Main){
                paymentApiState.value = response
                if(response.isSuccessful){
                    paymentStatus.value = PaymentStatus.RequestSentState
                }else{
                    paymentStatus.value = PaymentStatus.RequestFailedState
                }
            }
        }
    }

    fun confirmOrder() {
        val stkData = paymentApiState.value?.data as StkConfirmationResponse

        viewModelScope.launch(Dispatchers.IO) {
            orderConfirmationApiState.postValue(AccountManagementRepository.confirmOrder(stkData.merchantRequestID))
        }
    }

    fun clearCartData() {
        LocalRepository.swipeRecords.likes.value?.clear()
    }

    sealed class PaymentStatus{
        object EmptyState : PaymentStatus()
        object RequestSentState : PaymentStatus()
        object RequestFailedState : PaymentStatus()
    }
}