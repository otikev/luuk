package com.elmenture.luuk.ui.main

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.elmenture.luuk.base.repositories.LocalRepository
import models.Spot

class MainActivityViewModel : ViewModel() {
    var cartItemsLiveData = MediatorLiveData<MutableSet<Spot>>()

    init {
        cartItemsLiveData.addSource(LocalRepository.swipeRecords.likes) { cartItemsLiveData.setValue(it)}
    }

}