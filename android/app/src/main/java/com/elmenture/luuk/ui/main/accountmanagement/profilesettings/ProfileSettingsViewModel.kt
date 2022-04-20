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
    var isChangeSavable = MutableLiveData<Boolean>(false)

    init {
        userDetails.addSource(LocalRepository.userDetailsLiveData) { userDetails.postValue(it) }
    }

    fun updateUserData(updateUserDetailsRequest: UpdateUserDetailsRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            updateUserApiState.postValue(
                AccountManagementRepository.updateUserDetails(
                    updateUserDetailsRequest
                )
            )
        }
    }

    fun onCurrentDetailsChanged(uiUserDetails: UpdateUserDetailsRequest) {
        val persistedUserDetails = userDetails.value
        if (uiUserDetails.contactPhoneNumber != persistedUserDetails?.contactPhoneNumber) {
            isChangeSavable.value = true
            return
        }
        if (uiUserDetails.email != persistedUserDetails?.email) {
            isChangeSavable.value = true
            return
        }
        if (uiUserDetails.physicalAddress != persistedUserDetails?.physicalAddress) {
            isChangeSavable.value = true
            return
        }
        if (uiUserDetails.name !=persistedUserDetails?.name) {
            isChangeSavable.value = true
            return
        }
        if (uiUserDetails.targets != persistedUserDetails?.clothingRecommendations) {
            isChangeSavable.value = true
            return
        }

        isChangeSavable.value = false
    }

    private fun String?.comparesTo(compare: String?): Boolean {
        var str = this
        if (str == null) {
            str = ""
        }
        return str == compare
    }
}