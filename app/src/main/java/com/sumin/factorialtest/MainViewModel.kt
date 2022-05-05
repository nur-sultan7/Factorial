package com.sumin.factorialtest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.math.BigInteger
import kotlin.math.floor

class MainViewModel : ViewModel() {
    private val coroutineScope =
        CoroutineScope(Dispatchers.Main + CoroutineName("My coroutine scope"))
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
            withContext(Dispatchers.Default) {
                getFactorial(value.toLong())
            }
        }
        viewModelScope.launch {
            _state.value = Factorial(scope.await())
        }
    }

    private fun getFactorial(value: Long): String {
        var resultString = "Unknown"
        var result = BigInteger.ONE
        val percentOfValue = value / 10f
        var progressTrigger = percentOfValue
        for (i in 1..value) {
            result = result.multiply(BigInteger.valueOf(i))
            if (i == value) {
                resultString = result.toString()
            }
            if (i >= progressTrigger) {
                _state.postValue(Progress(i.toInt()))
                progressTrigger += percentOfValue
                progressTrigger = floor(progressTrigger * 10) / 10f
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