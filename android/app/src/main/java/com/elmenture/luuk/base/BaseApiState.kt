package com.elmenture.luuk.base

class BaseApiState(
    var data: Any? = null,
    var responseCode: Int? = null,
    var errorMessage: String? = null,
    var currentState: State? = null,
) {
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