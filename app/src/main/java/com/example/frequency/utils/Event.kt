package com.example.frequency.utils

/**
 * Class wrapper.
 * It cuts off repeated data in LD observers
 * */
class Event<T>(
    private val value: T
) {
    private var handle: Boolean = false

    fun getValue(): T? {
        if (handle) return null
        handle = true
        return value
    }
}