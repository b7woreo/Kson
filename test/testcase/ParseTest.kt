package testcase

import parse.Kson
import test.assertEqual
import test.assertError
import test.assertNull
import java.util.*

class ParseTest {
    private val jsonParse = Kson()

    fun testParseNull() {
        assertNull(input("null"))
    }

    fun testParseFalse() {
        val o = input("false")
        assertEqual(true, o is Boolean)
        assertEqual(false, o as Boolean)
    }

    fun testParseTrue() {
        val o = input("true")
        assertEqual(true, o is Boolean)
        assertEqual(true, o as Boolean)
    }

    fun testParseNumber() {
        assertNumber(0.0, "0")
        assertNumber(-0.0, "-0")
        assertNumber(-0.0, "-0.0")
        assertNumber(1.0, "1")
        assertNumber(-1.0, "-1")
        assertNumber(1.5, "1.5")
        assertNumber(-1.5, "-1.5")
        assertNumber(3.1416, "3.1416")
        assertNumber(1.5, "1.5")
        assertNumber(1.5, "1.5")
        assertError(IllegalArgumentException::class.java) {
            input("NAN")
        }
        assertError(IllegalArgumentException::class.java) {
            input("INF")
        }
    }

    private fun assertNumber(except: Double, input: String) {
        val o = input(input)
        assertEqual(true, o is Double)
        assertEqual(except, o as Double)
    }

    fun testParseString() {
        val o = input("\"Hello\"")
        assertEqual(true, o is String)
        assertEqual("Hello", o as String)
    }

    fun testParseArray() {
        val v1 = input("[ null , false , true , 123 , \"abc\" ]")
        assertEqual(true, v1 is ArrayList<*>)
        v1 as ArrayList<*>
        assertNull(v1[0])
        assertEqual(false, v1[1])
        assertEqual(true, v1[2])
        assertEqual(123.0, v1[3])
        assertEqual("abc", v1[4])

        val v2 = input("[ [ ] , [ 0 ] , [ 0 , 1 ] , [ 0 , 1 , 2 ] ]")
        assertEqual(true, v2 is ArrayList<*>)
        v2 as ArrayList<*>
        assertEqual(0, ((v2[0] as ArrayList<*>).size))
        assertEqual(1, ((v2[1] as ArrayList<*>).size))
        assertEqual(2, ((v2[2] as ArrayList<*>).size))
        assertEqual(3, ((v2[3] as ArrayList<*>).size))
    }

    fun testParseObject() {
        val v1 = input("{\"1\":1,\"2\":2,\"3\":3}")
        assertEqual(true, v1 is Map<*, *>)
        v1 as Map<*, *>
        assertEqual(true, v1.containsKey("1"))
        assertEqual(1.0, v1.get("1") as Double)
        assertEqual(true, v1.containsKey("2"))
        assertEqual(2.0, v1.get("2") as Double)
        assertEqual(true, v1.containsKey("3"))
        assertEqual(3.0, v1.get("3") as Double)
    }

    private fun input(json: String): Any? {
        return jsonParse.parse(json)
    }
}