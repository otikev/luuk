package com.elmenture.luuk.repositories

import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.base.BaseRepository

object SignInRepository {

    fun signInWithFacebook(request: HashMap<String, String>): BaseApiState {
        return BaseRepository.signInWithGoogle(request)
    }

    fun signInWithGoogle(request: HashMap<String, String>): BaseApiState {
        return BaseRepository.signInWithGoogle(request)
    }
}