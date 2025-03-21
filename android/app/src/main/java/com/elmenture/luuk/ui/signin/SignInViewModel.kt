package com.elmenture.luuk.ui.signin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmenture.luuk.base.BaseApiState
import com.elmenture.luuk.repositories.SignInRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {
    var signUpApiState = MutableLiveData<BaseApiState>(null)

    fun signInWithGoogle(map: HashMap<String, String>) {
        viewModelScope.launch(Dispatchers.IO) {
            signUpApiState.postValue(SignInRepository.signInWithGoogle(map))
        }
    }

    fun signInWithFacebook(map: HashMap<String, String>) {
        viewModelScope.launch(Dispatchers.IO) {
            signUpApiState.postValue(SignInRepository.signInWithFacebook(map))
        }
    }
}