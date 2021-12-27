package com.rams.jenkinsapp

import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(JUnit4::class)
class CalculatorTestCase {
    @Test
    fun addition_isCorrect() {
        assertEquals(7,Calculator.addition(3,4))
    }
    @Test
    fun multiplication_isCorrect(){
        assertEquals(4, Calculator.multiplication(2,2))
    }
    @Test
    fun subtraction_isCorrect() {
        assertEquals(1,  Calculator.subtraction(3,2));
    }

}