package com.elmenture.luuk.ui.main.home

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
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
    var itemsApiState: MutableLiveData<BaseApiState> = MutableLiveData(null)
    var itemsLiveData = MediatorLiveData<ArrayList<Item>>()

    init {
        itemsLiveData.addSource(LocalRepository.itemListLiveData) { value ->
            itemsLiveData.setValue(value)
        }
    }

    fun updateSwipesData(like: Spot? = null, dislike: Spot? = null) {
        val swipeData = if (LocalRepository.swipeDetailsLiveData.value == null) SwipeRecords() else LocalRepository.swipeDetailsLiveData.value
        like?.let { swipeData!!.likes.add(it) }
        dislike?.let { swipeData!!.dislikes.add(it) }
        LocalRepository.swipeDetailsLiveData.postValue(swipeData)
    }

    fun fetchItems(size: Int=15) {
        viewModelScope.launch(Dispatchers.IO) {
            itemsApiState.postValue(HomeRepository.fetchItems(size = size))
        }
    }
}