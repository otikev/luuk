package com.elmenture.luuk.repositories

import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.base.BaseRepository

object HomeRepository {

    fun fetchItems(): BaseApiState {
        //TODO: currently only fetching first page with 100 items. Will implement
        // background requests to fetch next pages as user swipes items
        return BaseRepository.fetchItemsPaginated(0,100)
    }

}