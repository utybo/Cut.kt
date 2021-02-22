import test_target.*
import cutkt.*
import kotlinx.cinterop.*
import kotlin.system.*

fun main() {
    val addOk = Add()
    val subOk = Sub()
    if (!addOk || !subOk)
        exitProcess(1)
}

fun Sub() = testsuite("Sub") {
    test("Simple subtraction") {
        assertEqual(10, sub(20, 10))
    }
    test("Harder subtraction") {
        assertEqual(-10, sub(10, 20))
    }
}

// The addition uses a struct as the result
fun Add() = testsuite("Add") {
    test("Add two positive integers") {
        // The invocation on the result of add is used for
        // getting the value out of the CValue we get
        // See Cut.kt
        assertEqual(5, add(1, 4)().result)
    }
    test("Add a positive and a negative integer") {
        assertEqual(5, add(-4, 9)().result)
    }
    test("Add two negative integers") {
        assertEqual(-10, add(-4, -6)().result)
    }
    test("Oops!") {
        assertEqual(3, add(1, 1)().result)
    }
}
