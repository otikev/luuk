package com.elmenture.luuk.ui.main.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmenture.luuk.base.repositories.RemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import models.Item

class SearchViewModel : ViewModel() {
    val searchItemLiveData = MutableLiveData<ArrayList<Item>>()

    fun fetchSearchItems(searchItem: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = RemoteRepository.fetchSearchItems(searchItem)
            withContext(Dispatchers.Main) {
                if(response.isSuccessful){
                    searchItemLiveData.value = response.data as ArrayList<Item>
                }
            }
        }
    }
}