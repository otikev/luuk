package com.elmenture.luuk.repositories

import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.base.BaseRepository
import com.luuk.common.models.BodyMeasurements

object AccountManagementRepository {

    fun postBodyMeasurements(request: BodyMeasurements): BaseApiState {
        return BaseRepository.doPostUserBodyMeasurements(request)
    }
}