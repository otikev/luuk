package com.elmenture.luuk.ui

import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.base.BaseRepository
import com.luuk.common.models.BodyMeasurements

object MyAccountRepository {

    fun postBodyMeasurements(request: BodyMeasurements): BaseApiState {
        return BaseRepository.doPostUserBodyMeasurements(request)
    }
}