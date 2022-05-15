package com.elmenture.luuk.ui.main.home

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmenture.luuk.base.repositories.LocalRepository
import com.elmenture.luuk.base.repositories.RemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import models.*

class HomeViewModel : ViewModel() {
    var actions: Action = Action()
    var itemsLiveData = MediatorLiveData<ArrayList<Item>>()

    init {
        itemsLiveData.addSource(LocalRepository.itemListLiveData) { itemsLiveData.setValue(it) }
    }


    fun updateSwipesData(like: Spot? = null, dislike: Spot? = null) {
        val likeData = LocalRepository.swipeRecords.likes.value
        val dislikeData = LocalRepository.swipeRecords.dislikes.value

        like?.let {
            actions.likes.add(it.itemId)
            likeData?.add(it)
            LocalRepository.swipeRecords.likes.postValue(likeData)
        }
        dislike?.let {
            actions.dislikes.add(it.itemId)
            dislikeData?.add(it)
            LocalRepository.swipeRecords.dislikes.postValue(dislikeData)
        }

        //More than 3 items have been actioned from the queue
        if (actions.totalActionCount() > 3) {
            viewModelScope.launch(Dispatchers.IO) {
                RemoteRepository.logUserActions(actions)
                actions.reset()
            }
        }
    }

    fun fetchItems(showAllItems: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = RemoteRepository.fetchItemsQueue(!showAllItems)
            if (response.isSuccessful) {
                LocalRepository.updateItemList(response.data as ArrayList<Item>?)
            }
        }
    }
}