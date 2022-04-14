package com.elmenture.luuk.ui.main.accountmanagement.mysizes

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.base.repositories.LocalRepository
import com.elmenture.luuk.repositories.AccountManagementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import models.ActualMeasurements

class MySizesViewModel : ViewModel() {
    var updateUserMeasurementsApiState = MutableLiveData<BaseApiState>(null)
    var userMeasurements =  MediatorLiveData<ActualMeasurements>()
    init {
        userMeasurements.addSource(LocalRepository.userDetailsLiveData) { userMeasurements.setValue(it.actualMeasurements)}
    }

    fun updateUserMeasurements(request: ActualMeasurements) {
        viewModelScope.launch(Dispatchers.IO) {
            updateUserMeasurementsApiState.postValue(AccountManagementRepository.postBodyMeasurements(request))
        }
    }
}