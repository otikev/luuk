package com.elmenture.luuk.repositories

import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.base.repositories.RemoteRepository

object SignInRepository {

    fun signInWithFacebook(request: HashMap<String, String>): BaseApiState {
        return RemoteRepository.signInWithFacebook(request)
    }

    fun signInWithGoogle(request: HashMap<String, String>): BaseApiState {
        return RemoteRepository.signInWithGoogle(request)
    }
}