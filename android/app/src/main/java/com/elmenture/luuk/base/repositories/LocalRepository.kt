package com.elmenture.luuk.base.repositories

import androidx.lifecycle.MutableLiveData
import models.Item
import models.SignInResponse
import models.SwipeRecords

object LocalRepository {
    var itemListLiveData = MutableLiveData<ArrayList<Item>>(arrayListOf())
    var userDetailsLiveData = MutableLiveData<SignInResponse>()
    var swipeDetailsLiveData = MutableLiveData<SwipeRecords>()
        get() {
            if (field.value == null) {
                field = MutableLiveData(SwipeRecords())
            }
            return field
        }

    fun updateUserDetails(userDetails: SignInResponse?){
        userDetailsLiveData.postValue(userDetails)
    }

    fun updateItemList(itemList: ArrayList<Item>?){
        val currentItemSet = itemListLiveData.value
        itemList?.let {
            currentItemSet?.addAll(itemList)
            itemListLiveData.postValue(currentItemSet)
        }
    }
}