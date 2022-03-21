package com.elmenture.luuk.ui.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.repositories.AccountManagementRepository
import com.elmenture.luuk.repositories.HomeRepository
import models.BodyMeasurements
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import models.Item

class HomeViewModel : ViewModel() {
    var itemsLiveData = MutableLiveData(ArrayList<Item>())
    var itemsApiState: MutableLiveData<BaseApiState> = MutableLiveData(null)

    fun fetchItems() {
      viewModelScope.launch(Dispatchers.IO) {
            val response = HomeRepository.fetchItems()
            withContext(Dispatchers.Main) {
              if(response.isSuccessful){
                  itemsLiveData.value = response.data as ArrayList<Item>
              }
            }
        }
    }
}