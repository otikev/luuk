package com.elmenture.luuk.repositories

import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.base.repositories.LocalRepository
import com.elmenture.luuk.base.repositories.RemoteRepository
import models.Item
import models.UpdateUserDetailsRequest
import models.ActualMeasurements
import java.util.ArrayList

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

    fun fetchItems(): BaseApiState {
        return RemoteRepository.fetchAllItems()
    }

    private fun handleUpdateUserDetailsSuccess(updateDetails: UpdateUserDetailsRequest) {
        val userDetails = LocalRepository.userDetailsLiveData.value

        updateDetails.name?.let { userDetails?.name = it }
        updateDetails.email?.let { userDetails?.email = it }
        updateDetails.contactPhoneNumber?.let { userDetails?.contactPhoneNumber = it }
        updateDetails.targets?.let { userDetails?.clothingRecommendations = it }
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

    fun updateItem(request: Item): BaseApiState {
        return RemoteRepository.updateItem(request)
    }

    fun validateCartItems(cartItems: ArrayList<Long>):BaseApiState {
        return RemoteRepository.validateCartItems(cartItems)
    }

    fun confirmOrder(merchantRequestID: String):BaseApiState {
        return RemoteRepository.confirmOrder(merchantRequestID)
    }

    fun fetchAllOrders(): BaseApiState {
        return RemoteRepository.fetchAllOrders()
    }

    fun fetchOrdersByState(state:String): BaseApiState {
        return RemoteRepository.fetchOrdersByState(state)
    }
}