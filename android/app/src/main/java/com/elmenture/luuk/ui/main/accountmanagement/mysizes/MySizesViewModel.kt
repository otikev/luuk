package com.elmenture.luuk.ui.main.accountmanagement.mysizes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.repositories.AccountManagementRepository
import com.luuk.common.models.BodyMeasurements
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MySizesViewModel : ViewModel() {
    var bodyMeasurementsLiveData = MutableLiveData<BodyMeasurements>(BodyMeasurements())
    var postBodyMeasurementsApiState: MutableLiveData<BaseApiState> = MutableLiveData(null)

    fun updateBodyMeasurements(bodyMeasurements: BodyMeasurements) {
      bodyMeasurementsLiveData.value = bodyMeasurements

      viewModelScope.launch(Dispatchers.IO) {
            val response = AccountManagementRepository.postBodyMeasurements(bodyMeasurements)
            withContext(Dispatchers.Main) {
              postBodyMeasurementsApiState.value = response
            }
        }
    }
}