package com.sumin.factorialtest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    fun calculate(value: String?) {
        _state.value = State()
        if (value.isNullOrBlank()) {
            _state.value = State(isError = false)
            return
        }
        viewModelScope.launch {
            val number = value.toLong()
            for (i in 1..number) {
                _state.value = State(progress = i.toInt())
                delay(20)
            }
            _state.value = _state.value?.copy(factorial = "300")
        }
    }
}