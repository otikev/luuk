package com.elmenture.luuk.ui.main

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.elmenture.luuk.base.repositories.LocalRepository
import models.SwipeRecords

class MainActivityViewModel : ViewModel() {
    var swipeRecordsLiveData = MediatorLiveData<SwipeRecords>()

    init {
        swipeRecordsLiveData.addSource(LocalRepository.swipeDetailsLiveData) { swipeRecordsLiveData.setValue(it)}
    }

}