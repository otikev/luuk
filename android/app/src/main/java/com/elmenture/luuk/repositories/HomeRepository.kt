package com.elmenture.luuk.repositories

import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.base.repositories.LocalRepository
import com.elmenture.luuk.base.repositories.RemoteRepository
import models.ItemResponse

object HomeRepository {
    private var currentPage: Int = 0

    fun fetchItems(page: Int = currentPage, size: Int = 100): BaseApiState {
        val response = RemoteRepository.fetchItemsPaginated(page, size)
        if (response.isSuccessful) {
            val item = response.data as ItemResponse
            if (!item.last) {
                LocalRepository.updateItemList(item.content)
                currentPage++
            }
        }
        return response
    }

}