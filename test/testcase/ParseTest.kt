package testcase

import json.JSONInput
import json.JSONParse
import json.JSONType
import test.assertEqual
import test.assertError

class ParseTest {
    fun testParseNull() {
        assertEqual(JSONType.Null, input("null"))
    }

    fun testParseFalse() {
        assertEqual(false, input("false").value!!)

    }

    fun testParseTrue() {
        val p = JSONParse(JSONInput("true"))
        assertEqual(JSONType.Boolean(true).value, p.parse().value!!)
    }

    fun testParseNumber() {
        assertEqual(0.0, input("0").value!!)
        assertEqual(-0.0, input("-0").value!!)
        assertEqual(-0.0, input("-0.0").value!!)
        assertEqual(1.0, input("1").value!!)
        assertEqual(-1.0, input("-1").value!!)
        assertEqual(1.5, input("1.5").value!!)
        assertEqual(-1.5, input("-1.5").value!!)
        assertEqual(3.1416, input("3.1416").value!!)
        assertEqual(1.5, input("1.5").value!!)
        assertEqual(1.5, input("1.5").value!!)
        assertError(IllegalArgumentException::class.java) {
            input("NAN")
        }
        assertError(IllegalArgumentException::class.java) {
            input("INF")
        }
    }

    fun testParseString() {
        assertEqual(JSONType.String("Hello").value, input("\"Hello\"").value!!)
    }

    private fun input(json: String): JSONType<*> {
        return JSONParse(JSONInput(json)).parse()
    }
}