package com.elmenture.luuk.ui.mysizes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luuk.common.models.BodyMeasurements

class MySizesViewModel : ViewModel(){
  var bodyMeasurements = MutableLiveData<BodyMeasurements>(BodyMeasurements())

}