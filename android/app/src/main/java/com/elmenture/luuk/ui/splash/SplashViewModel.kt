package com.elmenture.luuk.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.base.repositories.RemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    val userDetailsApiState = MutableLiveData<BaseApiState>()

    fun fetchUserDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            userDetailsApiState.postValue(RemoteRepository.fetchUserDetails())
        }
    }

}