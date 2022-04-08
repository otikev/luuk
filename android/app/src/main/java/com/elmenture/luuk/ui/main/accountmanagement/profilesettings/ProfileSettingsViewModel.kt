package com.elmenture.luuk.ui.main.accountmanagement.profilesettings

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.base.repositories.LocalRepository
import com.elmenture.luuk.repositories.AccountManagementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import models.SignInResponse
import models.UpdateUserDetailsRequest

class ProfileSettingsViewModel : ViewModel() {
    var userDetails = MediatorLiveData<SignInResponse>()
    var updateUserApiState = MutableLiveData<BaseApiState>()

    init {
        userDetails.addSource(LocalRepository.userDetailsLiveData){userDetails.postValue(it)}
    }

    fun updateUserData(updateUserDetailsRequest: UpdateUserDetailsRequest){
        viewModelScope.launch(Dispatchers.IO) {
            updateUserApiState.postValue(AccountManagementRepository.updateUserDetails(updateUserDetailsRequest))
        }
    }
}