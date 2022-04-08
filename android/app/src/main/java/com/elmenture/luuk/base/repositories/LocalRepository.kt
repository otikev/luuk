package com.elmenture.luuk.base.repositories

import androidx.lifecycle.MutableLiveData
import models.SignInResponse
import models.SwipeRecords

object LocalRepository {
    var userDetails = MutableLiveData<SignInResponse>()
    var swipeDetails = MutableLiveData<SwipeRecords>()
        get() {
            if (field.value == null) {
                field = MutableLiveData(SwipeRecords())
            }
            return field
        }


}