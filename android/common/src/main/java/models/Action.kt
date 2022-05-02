package models

import com.google.gson.annotations.SerializedName

data class Action(
    @field:SerializedName("likes")
    var likes: MutableList<Long> = mutableListOf(),

    @field:SerializedName("dislikes")
    var dislikes: MutableList<Long> = mutableListOf()
)

fun Action.reset() {
    this.likes = mutableListOf()
    this.dislikes = mutableListOf()
}

fun Action.totalActionCount(): Int {
    return this.likes.size+this.dislikes.size
}