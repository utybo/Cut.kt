package cutkt

import kotlinx.cinterop.*

/**
 * Reveiver type for a single test.
 */
class TestingContext(val testsuiteName: String, val testName: String)

/**
 * Exception representing a test failure (e.g. a failed assertion)
 */
open class TestFailure(message: String) : Exception(message)

/**
 * Reveiver type for an entire testsuite.
 *
 * Has innate memory management. In order to use a CValue, simply invoke it
 * (e.g. my_cvalue()).
 */
class TestsuiteContext(val testsuiteName: String) {
    var errors = 0
    var pass = 0
    val arena = Arena()

    operator inline fun <reified T: CStructVar> CValue<T>.invoke(): T {
        val ptr = this.placeTo(arena)
        return ptr.pointed
    }
}

// --- Color escape codes ---
const val RS = "\u001b[0m"
const val R = "\u001b[31m"
const val G = "\u001b[32m"
const val Y = "\u001b[33m"

/**
 * Declares and runs a testsuite with an optional name.
 */
fun testsuite(name: String = "Testsuite", block: TestsuiteContext.() -> Unit): Boolean {
    println("${Y}=== TESTSUITE: $name ===${RS}")
    val ctx = TestsuiteContext(name).apply(block)
    print("${Y}=== $name: ")
    if (ctx.errors == 0 && ctx.pass == 0)
        print("${R}No test ran!${Y}")
    else {
        val messages = mutableListOf<String>()
        if (ctx.errors > 0)
            messages += "${R}${ctx.errors} failing"
        if (ctx.pass > 0)
            messages += "${G}${ctx.pass} passing"
        print(messages.joinToString("${Y}, "))
    }
    println("${Y} ===${RS}")
    ctx.arena.clear()
    return ctx.errors == 0
}

/**
 * A single test
 */
fun TestsuiteContext.test(name: String, block: TestingContext.() -> Unit) {
    try {
        TestingContext(testsuiteName, name).apply(block)
        pass++
        println("${G}PASS:${RS} $name")
    } catch (ex: TestFailure) {
        print("${R}FAILURE:${RS} $name, ")
        println(ex.message!!)
        errors++
    }
}

/**
 * Asserts that 'expected' is equal to 'actual'
 */
fun TestingContext.assertEqual(expected: Int, actual: Int) {
    if (expected != actual) {
        throw TestFailure("Expected $expected but got $actual")
    }
}
