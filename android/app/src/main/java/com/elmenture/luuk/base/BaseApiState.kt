package com.elmenture.luuk.base

class BaseApiState(
    var data: Any? = null,
    var errorCode: Int? = null,
    var errorMessage: String? = null,
    var currentState: State? = null,
    var isLoading: Boolean = (currentState == State.LOADING)
) {

    enum class State {
        LOADING, SUCCESS, FAILED;
    }
    companion object {
        var ERROR_STATE: BaseApiState = BaseApiState(currentState = State.FAILED)
        var LOADING_STATE: BaseApiState = BaseApiState(currentState = State.LOADING)
        var SUCCESS_STATE: BaseApiState = BaseApiState(currentState = State.SUCCESS)
    }
}