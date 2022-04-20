package com.elmenture.luuk.ui.main.accountmanagement.inventorymanagement

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.repositories.AccountManagementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class InventoryManagementViewModel : ViewModel() {

    var fetchItemsApiState: MutableLiveData<BaseApiState> = MutableLiveData(null)

    fun fetchItems() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchItemsApiState.postValue(AccountManagementRepository.fetchItems())
        }
    }
}

