package com.elmenture.luuk.ui.main.cart

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmenture.luuk.base.repositories.LocalRepository
import com.elmenture.luuk.repositories.AccountManagementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import models.Spot

class CartViewModel : ViewModel() {

    var cartItemsLiveData = MediatorLiveData<MutableSet<Spot>>()

    init {
        cartItemsLiveData.addSource(LocalRepository.swipeRecords.likes) {
            cartItemsLiveData.setValue(
                it
            )
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
                AccountManagementRepository.validateCartItems(getCartItems())
        }
    }

}