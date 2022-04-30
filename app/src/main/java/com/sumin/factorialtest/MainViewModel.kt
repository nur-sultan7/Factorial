package com.sumin.factorialtest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.math.BigInteger
import kotlin.concurrent.thread
import kotlin.coroutines.suspendCoroutine

class MainViewModel : ViewModel() {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    fun calculate(value: String?) {
        _state.value = Progress(0)
        if (value.isNullOrBlank()) {
            _state.value = Error
            return
        }
        viewModelScope.launch {
            val factorial = getFactorial(value.toLong())
            _state.value = Factorial(factorial)
        }
    }

    private suspend fun getFactorial(value: Long): String {
        return suspendCoroutine {
            thread {
                var resultString = "Unknown"
                var result = BigInteger.ONE
                val percentOfValue = value / 10
                var progressTrigger = percentOfValue
                for (i in 1..value) {
                    result = result.multiply(BigInteger.valueOf(i))
                    if (i == progressTrigger) {
                        if (i == value)
                            resultString = result.toString()
                        _state.postValue(Progress(i.toInt()))
                        progressTrigger += percentOfValue
                    }
                }
                it.resumeWith(Result.success(resultString))
            }
        }
    }
}