package com.elmenture.luuk.repositories

import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.base.repositories.LocalRepository
import com.elmenture.luuk.base.repositories.RemoteRepository
import com.google.gson.Gson
import models.SignInResponse

object SignInRepository {

    fun signInWithFacebook(request: HashMap<String, String>): BaseApiState {
        val userDetails = RemoteRepository.signInWithFacebook(request)
        if (userDetails.isSuccessful) {
            LocalRepository.updateUserDetails(userDetails.getData())
        }
        return userDetails
    }

    fun signInWithGoogle(request: HashMap<String, String>): BaseApiState {
        val userDetails = RemoteRepository.signInWithGoogle(request)
        if (userDetails.isSuccessful) {
            LocalRepository.updateUserDetails(userDetails.getData() as SignInResponse)
        }
        return userDetails
    }
}