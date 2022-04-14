package com.elmenture.luuk.repositories

import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.base.repositories.LocalRepository
import com.elmenture.luuk.base.repositories.RemoteRepository
import models.Item
import models.UpdateUserDetailsRequest
import models.ActualMeasurements

object AccountManagementRepository {

    fun postBodyMeasurements(request: ActualMeasurements): BaseApiState {
        val response = RemoteRepository.doPostUserBodyMeasurements(request)
        if (response.isSuccessful)
            handlePostBodyMeasurementSuccess(request)
        return response
    }

    fun getUserBodyMeasurements(): BaseApiState {
        return RemoteRepository.getUserBodyMeasurements()
    }

    fun createNewItem(request: Item): BaseApiState {
        return RemoteRepository.createNewItem(request)
    }

    fun updateUserDetails(request: UpdateUserDetailsRequest): BaseApiState {
        val response = RemoteRepository.updateUserDetails(request)
        if (response.isSuccessful)
            handleUpdateUserDetailsSuccess(request)
        return response
    }

    private fun handleUpdateUserDetailsSuccess(updateDetails: UpdateUserDetailsRequest) {
        val userDetails = LocalRepository.userDetailsLiveData.value

        updateDetails.name?.let { userDetails?.name = it }
        updateDetails.email?.let { userDetails?.email = it }
        updateDetails.contactPhoneNumber?.let { userDetails?.contactPhoneNumber = it }
        updateDetails.gender?.let { userDetails?.gender = it }
        updateDetails.physicalAddress?.let { userDetails?.physicalAddress = it }

        userDetails?.let { LocalRepository.updateUserDetails(it) }
    }


    private fun handlePostBodyMeasurementSuccess(measurements: ActualMeasurements) {
        val userDetails =
            LocalRepository.userDetailsLiveData.value//User.getCurrent().userDetails.userMeasurements
        measurements.bodyMeasurements?.let {
            userDetails?.actualMeasurements?.bodyMeasurements = it
        }
        measurements.clothingSizes?.let {
            userDetails?.actualMeasurements?.clothingSizes = it
        }
        userDetails?.let { LocalRepository.updateUserDetails(it) }
    }
}