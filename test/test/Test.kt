package test

import testcase.ParseTest
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass
import kotlin.reflect.declaredMemberFunctions
import kotlin.reflect.primaryConstructor

fun main(args: Array<String>) {
    Test.test(ParseTest::class)
}

class Test() {
    companion object {
        private val FORMAT_FAILURE = "FAILURE: %s#%s -- %s"
        private val FORMAT_FINISH = "TOTAL: %d, SUCCESS: %d, FAILURE: %d, PASS: %d%%"

        fun test(clz: KClass<*>) {
            val t = clz.primaryConstructor?.call()
            if (t == null) (throw RuntimeException("clz must have none argument constructor"))

            var success = 0
            var failure = 0

            clz.declaredMemberFunctions.forEach { it ->
                try {
                    if (it.name.startsWith("test") && it.parameters.size == 1) {
                        it.call(t)
                        success += 1
                    }
                } catch (e: InvocationTargetException) {
                    if (e.targetException is AssertException) {
                        println(String.format(FORMAT_FAILURE, clz.qualifiedName, it.name, e.targetException.message))
                        failure += 1
                    } else {
                        throw e
                    }
                }
            }

            val total = success + failure
            println(String.format(FORMAT_FINISH, total, success, failure, (success * 100 / total)))
        }
    }
}

fun assertEqual(except: Any, actual: Any) {
    if (except != actual) {
        throw AssertException("except: " + except.toString() + ", actual: " + actual)
    }
}

fun <T : Exception> assertError(except: Class<T>, test: () -> Unit) {
    try {
        test()
        throw RuntimeException()
    } catch (e: RuntimeException) {
        if (e.javaClass != except) {
            throw AssertException("except error: " + except.name)
        }
    }
}

class AssertException(message: String) : RuntimeException(message)