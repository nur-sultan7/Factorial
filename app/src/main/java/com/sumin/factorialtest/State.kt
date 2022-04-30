package com.sumin.factorialtest

sealed class State
object Error : State()
class Progress(val value: Int) : State()
class Factorial(val value: String) : State()