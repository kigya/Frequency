package com.example.frequency

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun flowsTest() = runBlocking {
        val num = 1..10

        val numFlow = num.asFlow()
        numFlow.filter { it%2 ==0 }.map { it*2 }.collect{
            println(it)
        }

    }
}