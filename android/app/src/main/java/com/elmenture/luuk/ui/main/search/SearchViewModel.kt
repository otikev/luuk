package com.elmenture.luuk.ui.main.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.base.repositories.RemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import models.Item
import models.TagProperty

class SearchViewModel : ViewModel() {
    val searchTagLiveData = MutableLiveData<ArrayList<TagProperty>>()
    val searchItemsByTagLiveData = MutableLiveData<ArrayList<Item>>()

    fun fetchSearchItems(searchItem: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = RemoteRepository.fetchSearchTags(searchItem)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    searchTagLiveData.value = response.data as ArrayList<TagProperty>
                }
            }
        }
    }

    fun fetchItemsByTag(tagPropertyId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = RemoteRepository.fetchItemsByTagProperty(tagPropertyId)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    searchItemsByTagLiveData.value = response.data as ArrayList<Item>
                }
            }
        }
    }
}