package com.elmenture.luuk.repositories

import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.base.BaseRepository

object HomeRepository {

    fun fetchItems(): BaseApiState {
        return BaseRepository.fetchItems()
    }

}