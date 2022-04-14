package models

import androidx.lifecycle.MutableLiveData

class SwipeRecords {
    val likes: MutableLiveData<MutableSet<Spot>> = MutableLiveData(mutableSetOf())
    val dislikes: MutableLiveData<MutableSet<Spot>>  = MutableLiveData(mutableSetOf())
}
