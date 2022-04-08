package com.elmenture.luuk.repositories

import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.base.repositories.RemoteRepository
import models.Item
import models.UpdateUserDetailsRequest
import models.UserMeasurements

object AccountManagementRepository {

    fun postBodyMeasurements(request: UserMeasurements): BaseApiState {
        return RemoteRepository.doPostUserBodyMeasurements(request)
    }

    fun getUserBodyMeasurements(): BaseApiState {
        return RemoteRepository.getUserBodyMeasurements()
    }

    fun createNewItem(request: Item): BaseApiState {
        return RemoteRepository.createNewItem(request)
    }

    fun updateUserDetails(request: UpdateUserDetailsRequest): BaseApiState {
        return RemoteRepository.updateUserDetails(request)
    }
}