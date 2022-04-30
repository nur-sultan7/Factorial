package com.sumin.factorialtest

data class State(
    val isError: Boolean = false,
    val progress: Int = 0,
    val factorial: String = ""
)