package com.elmenture.luuk.base

@Suppress("UNCHECKED_CAST")
class BaseApiState(
    var responseCode: Int? = null,
    var errorMessage: String? = null,
    var currentState: State? = null,
) {
    private var data: Any? = null

    fun setData(data: Any?) {
        this.data = data
    }

    fun <T> getData(): T {
        return data as T
    }

    val isSuccessful: Boolean
        get() = currentState == State.SUCCESS

    enum class State {
        SUCCESS, FAILED;
    }

    companion object {
        var ERROR_STATE: BaseApiState = BaseApiState(currentState = State.FAILED)
        var SUCCESS_STATE: BaseApiState = BaseApiState(currentState = State.SUCCESS)
    }
}