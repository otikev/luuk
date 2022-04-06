package com.elmenture.luuk.repositories

import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.base.BaseRepository
import models.BodyMeasurements
import models.Item
import models.UserMeasurements

object AccountManagementRepository {

    fun postBodyMeasurements(request: UserMeasurements): BaseApiState {
        return BaseRepository.doPostUserBodyMeasurements(request)
    }

    fun getUserBodyMeasurements(): BaseApiState {
        return BaseRepository.getUserBodyMeasurements()
    }

    fun createNewItem(request: Item): BaseApiState {
        return BaseRepository.createNewItem(request)
    }
}