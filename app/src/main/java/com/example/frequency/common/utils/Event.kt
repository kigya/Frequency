package com.example.frequency.common.utils

/**
 * Class wrapper.
 * It cuts off repeated data in LD observers
 * */
class Event<T>(
    value: T
) {
    private var _value: T? = value

    fun get(): T? = _value.also { _value = null }
}