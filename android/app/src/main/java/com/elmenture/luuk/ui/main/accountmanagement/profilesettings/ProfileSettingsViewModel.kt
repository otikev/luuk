package com.elmenture.luuk.ui.main.accountmanagement.inventorymanagement

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.repositories.AccountManagementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import models.Item
import models.UpdateUserDetailsRequest
import models.UserMeasurements
import userdata.User

class ProfileSettingsViewModel : ViewModel() {
    var userDetails = MutableLiveData(User.getCurrent().userDetails)

    fun updateUserData(updateUserDetailsRequest: UpdateUserDetailsRequest){
        viewModelScope.launch(Dispatchers.IO) {
            val response = AccountManagementRepository.updateUserDetails(updateUserDetailsRequest)
            withContext(Dispatchers.Main){

            }
        }
    }
}