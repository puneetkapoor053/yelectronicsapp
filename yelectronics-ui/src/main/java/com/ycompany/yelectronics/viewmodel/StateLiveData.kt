package com.ycompany.yelectronics.viewmodel

import androidx.lifecycle.MutableLiveData

class StateLiveData<T> : MutableLiveData<StateData<T>?>() {

    fun postLoading() {
        postValue(StateData<T>().loading())
    }

    fun postError(error: Throwable) {
        postValue(StateData<T>().error(error))
    }

    fun postSuccess(data: T) {
        postValue(StateData<T>().success(data))
    }
}