package com.sumin.factorialtest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.math.BigInteger

class MainViewModel : ViewModel() {
    private val coroutineScope =
        CoroutineScope(
            Dispatchers.Default
                    + CoroutineName("Factorial scope")
        )
    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    fun calculate(value: String?) {
        _state.value = Progress(0)
        if (value.isNullOrBlank()) {
            _state.value = Error
            return
        }
        val scope = coroutineScope.async {
            getFactorial(value.toLong())
        }
        viewModelScope.launch {
            _state.value = Factorial(scope.await())
        }
    }

    private fun getFactorial(value: Long): String {
        var resultString = "Unknown"
        var result = BigInteger.ONE
        var lastUpdate = 0
        for (i in 1..value) {
            result = result.multiply(BigInteger.valueOf(i))
            if (i == value) {
                resultString = result.toString()
            }
            val currentProgress = (i / value.toFloat() * 100).toInt()
            if (currentProgress > lastUpdate) {
                lastUpdate = currentProgress
                _state.postValue(Progress(currentProgress))
            }
        }
        return resultString
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }

    //    private suspend fun getFactorial(value: Long): String {
//        return suspendCoroutine {
//            thread {
//                var resultString = "Unknown"
//                var result = BigInteger.ONE
//                val percentOfValue = value / 10
//                var progressTrigger = percentOfValue
//                for (i in 1..value) {
//                    result = result.multiply(BigInteger.valueOf(i))
//                    if (i == progressTrigger) {
//                        if (i == value)
//                            resultString = result.toString()
//                        _state.postValue(Progress(i.toInt()))
//                        progressTrigger += percentOfValue
//                    }
//                }
//                it.resumeWith(Result.success(resultString))
//            }
//        }
//    }
}