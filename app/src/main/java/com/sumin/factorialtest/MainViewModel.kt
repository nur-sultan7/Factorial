package com.sumin.factorialtest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean>
        get() = _error

    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int>
        get() = _progress

    private val _factorial = MutableLiveData<String>()
    val factorial: LiveData<String>
        get() = _factorial

    fun calculate(value: String?) {
        _progress.value = 0
        if (value.isNullOrBlank()) {
            _error.value = true
            return
        }
        viewModelScope.launch {
            val number = value.toLong()
            for (i in 1..number) {
                _progress.value = i.toInt()
                delay(20)
            }
            _factorial.value = "100"
        }
    }
}