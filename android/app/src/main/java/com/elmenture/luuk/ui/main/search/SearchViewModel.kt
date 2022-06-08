package com.elmenture.luuk.ui.main.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmenture.luuk.base.repositories.RemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import models.Item
import utils.CannedSearch

class SearchViewModel : ViewModel() {
    val searchItemsByQueryLiveData = MutableLiveData<ArrayList<Item>>()
    val cannedItemsLiveData = MutableLiveData<ArrayList<Item>>()

    fun fetchItemsByQuery(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = RemoteRepository.fetchItemsByQuery(query)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    searchItemsByQueryLiveData.value = response.getData()
                }
            }
        }
    }

    fun fetchCannedItems(query: CannedSearch) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = RemoteRepository.fetchCannedItems(query)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    cannedItemsLiveData.value = response.getData()
                }
            }
        }
    }
}