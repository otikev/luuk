package com.elmenture.luuk.ui.main

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.elmenture.luuk.base.repositories.LocalRepository
import models.SignInResponse
import models.Spot
import userdata.User

class MainActivityViewModel : ViewModel() {
    var cartItemsLiveData = MediatorLiveData<MutableSet<Spot>>()
    var profileDetailsLiveData = MediatorLiveData<SignInResponse>()

    init {
        cartItemsLiveData.addSource(LocalRepository.swipeRecords.likes) { cartItemsLiveData.setValue(it)}
        profileDetailsLiveData.addSource(LocalRepository.userDetailsLiveData){profileDetailsLiveData.setValue(it)}
    }

}