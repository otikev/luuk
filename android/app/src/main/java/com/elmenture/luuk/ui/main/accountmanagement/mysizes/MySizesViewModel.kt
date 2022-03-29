package com.elmenture.luuk.ui.main.accountmanagement.mysizes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.repositories.AccountManagementRepository
import models.BodyMeasurements
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import userdata.User
import java.net.CacheRequest

class MySizesViewModel : ViewModel() {
    var bodyMeasurementsLiveData = MutableLiveData(User.getCurrent().userDetails.bodyMeasurements)
    var postBodyMeasurementsApiState: MutableLiveData<BaseApiState> = MutableLiveData(null)

    fun updateBodyMeasurements(request: BodyMeasurements) {
        bodyMeasurementsLiveData.value = request
        viewModelScope.launch(Dispatchers.IO) {
            val response = AccountManagementRepository.postBodyMeasurements(request)
            withContext(Dispatchers.Main) {
              postBodyMeasurementsApiState.value = response
            }
        }
    }

}