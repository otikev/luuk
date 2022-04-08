package com.elmenture.luuk.ui.main.home

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.base.repositories.LocalRepository
import com.elmenture.luuk.repositories.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import models.Item
import models.ItemResponse
import models.Spot
import models.SwipeRecords

class HomeViewModel : ViewModel() {
    var itemsLiveData = MutableLiveData(ArrayList<Item>())
    var itemsApiState: MutableLiveData<BaseApiState> = MutableLiveData(null)
    var liveDataMerger = MediatorLiveData<SwipeRecords>()

    init {
        liveDataMerger.addSource(LocalRepository.swipeDetails) { value ->
            liveDataMerger.setValue(value)
        }
    }

    fun updateSwipesData(like: Spot? = null, dislike: Spot? = null) {
        val swipeData = if (LocalRepository.swipeDetails.value == null) SwipeRecords() else LocalRepository.swipeDetails.value
        like?.let { swipeData!!.likes.add(it) }
        dislike?.let { swipeData!!.dislikes.add(it) }
        LocalRepository.swipeDetails.postValue(swipeData)
    }

    fun fetchItems() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = HomeRepository.fetchItems()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val list = response.data as ItemResponse
                    itemsLiveData.value = list.content as ArrayList<Item>
                }
            }
        }
    }
}