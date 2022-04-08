package com.elmenture.luuk.base.repositories

import androidx.lifecycle.MutableLiveData
import models.SignInResponse
import models.SwipeRecords

object LocalRepository {
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
}