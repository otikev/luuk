package com.elmenture.luuk.base.repositories

import androidx.lifecycle.MutableLiveData
import models.Item
import models.SignInResponse
import models.SwipeRecords

object LocalRepository {
    var itemListLiveData = MutableLiveData<ArrayList<Item>>(arrayListOf())
    var userDetailsLiveData = MutableLiveData<SignInResponse>()
    var swipeRecords = SwipeRecords()

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