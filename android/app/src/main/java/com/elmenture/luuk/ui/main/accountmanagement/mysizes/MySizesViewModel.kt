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
import models.UserMeasurements

class MySizesViewModel : ViewModel() {
    var updateUserMeasurementsApiState = MutableLiveData<BaseApiState>(null)
    var userMeasurements =  MediatorLiveData<UserMeasurements>()
    init {
        userMeasurements.addSource(LocalRepository.userDetailsLiveData) { userMeasurements.setValue(it.userMeasurements)}
    }

    fun updateUserMeasurements(request: UserMeasurements) {
        viewModelScope.launch(Dispatchers.IO) {
            updateUserMeasurementsApiState.postValue(AccountManagementRepository.postBodyMeasurements(request))
        }
    }
}