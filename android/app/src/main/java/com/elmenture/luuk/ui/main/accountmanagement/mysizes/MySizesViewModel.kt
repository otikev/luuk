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
import models.UserMeasurements
import userdata.User
import java.net.CacheRequest

class MySizesViewModel : ViewModel() {
    //var bodyMeasurementsLiveData = MutableLiveData(User.getCurrent().userDetails.bodyMeasurements)
    var userMeasurements = MutableLiveData<UserMeasurements>(User.getCurrent().userDetails.userMeasurements)

    fun updateUserMeasurements(request: UserMeasurements) {
       // bodyMeasurementsLiveData.value = request
        viewModelScope.launch(Dispatchers.IO) {
            val response = AccountManagementRepository.postBodyMeasurements(request)
            withContext(Dispatchers.Main) {
              if(response.isSuccessful){
                  handleUpdateSuccess(request)
              }
            }
        }
    }

    private fun handleUpdateSuccess(measurements: UserMeasurements) {
        val userMeasurements = User.getCurrent().userDetails.userMeasurements
        measurements.bodyMeasurements?.let {
            userMeasurements?.bodyMeasurements = it
        }
        measurements.clothingSizes?.let {
            userMeasurements?.clothingSizes = it
        }

    }

}