package com.elmenture.luuk.ui.main.accountmanagement.inventorymanagement

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.repositories.AccountManagementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import models.Item

class CreateNewItemViewModel : ViewModel() {

    var createNewItemApiState: MutableLiveData<BaseApiState> = MutableLiveData(null)
    var updateItemApiState: MutableLiveData<BaseApiState> = MutableLiveData(null)

    fun createNewItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = AccountManagementRepository.createNewItem(item)
            withContext(Dispatchers.Main) {
                createNewItemApiState.value = response
            }
        }
    }

    fun updateItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = AccountManagementRepository.updateItem(item)
            withContext(Dispatchers.Main) {
                updateItemApiState.value = response
            }
        }
    }

}