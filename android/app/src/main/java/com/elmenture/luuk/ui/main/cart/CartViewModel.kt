package com.elmenture.luuk.ui.main.cart

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.base.repositories.LocalRepository
import com.elmenture.luuk.repositories.AccountManagementRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import models.Item
import models.Spot
import models.StkConfirmationResponse
import java.lang.reflect.Type

class CartViewModel : ViewModel() {

    var cartItemsLiveData = MediatorLiveData<MutableSet<Spot>>()
    var profileDetailsLiveData = MediatorLiveData<ProfileStatus>()

    var paymentApiState = MutableLiveData<BaseApiState>()
    var orderConfirmationApiState = MutableLiveData<BaseApiState>()
    var paymentStatus = MutableLiveData<PaymentStatus>(PaymentStatus.EmptyState)

    init {
        cartItemsLiveData.addSource(LocalRepository.swipeRecords.likes) {
            cartItemsLiveData.setValue(it)
        }
        profileDetailsLiveData.addSource(LocalRepository.userDetailsLiveData) {
            profileDetailsLiveData.value =
                if (it.email.isNullOrEmpty() || it.contactPhoneNumber.isNullOrEmpty() || it.physicalAddress.isNullOrEmpty()) ProfileStatus.Incomplete() else ProfileStatus.Complete()

        }
    }

    sealed class ProfileStatus() {
        class Incomplete : ProfileStatus()
        class Complete : ProfileStatus()
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
            withContext(Dispatchers.Main) {
                paymentApiState.value = response
                if (response.isSuccessful) {
                    paymentStatus.value = PaymentStatus.RequestSentState
                } else {
                    when (response.responseCode) {
                        400 -> {
                            paymentStatus.value = PaymentStatus.RequestFailedState
                        }
                        302 -> {
                            handleItemsAlreadySold(response.errorMessage)
                            paymentStatus.value =   PaymentStatus.ItemsSoldState
                        }
                    }
                }
            }
        }
    }

    private fun handleItemsAlreadySold(errorMessage: String?) {
        val soldItems = Gson().fromJson(errorMessage, Array<Item>::class.java).toList()
        val spotsToDiscard = arrayListOf<Spot>()


        for (soldItem: Item in soldItems) {
            for (spot: Spot in cartItemsLiveData.value!!) {
                if (spot.itemId == soldItem.id)
                    spotsToDiscard.add(spot)
            }
        }
        cartItemsLiveData.value!!.removeAll(spotsToDiscard)
        LocalRepository.swipeRecords.likes.postValue(cartItemsLiveData.value)
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

    sealed class PaymentStatus {
        object EmptyState : PaymentStatus()
        object RequestSentState : PaymentStatus()
        object RequestFailedState : PaymentStatus()
        object ItemsSoldState : PaymentStatus()
    }
}