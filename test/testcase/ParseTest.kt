package testcase

import json.JSONInput
import json.JSONParse
import json.JSONType
import test.assertEqual
import test.assertError

class ParseTest {
    private val jsonParse = JSONParse()

    fun testParseNull() {
        assertEqual(JSONType.Null, input("null"))
    }

    fun testParseFalse() {
        assertEqual(false, input("false").value!!)

    }

    fun testParseTrue() {
        assertEqual(JSONType.Boolean(true).value, input("true").value!!)
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

    fun testParseArray() {
        val v1 = input("[ null , false , true , 123 , \"abc\" ]").value!! as Array<JSONType<*>>
        assertEqual(JSONType.Null, v1[0])
        assertEqual(false, v1[1].value!!)
        assertEqual(true, v1[2].value!!)
        assertEqual(123.0, v1[3].value!!)
        assertEqual("abc", v1[4].value!!)

        val v2 = input("[ [ ] , [ 0 ] , [ 0 , 1 ] , [ 0 , 1 , 2 ] ]").value!! as Array<JSONType<*>>
        assertEqual(0, (v2[0].value!! as Array<*>).size)
        assertEqual(1, (v2[1].value!! as Array<*>).size)
        assertEqual(2, (v2[2].value!! as Array<*>).size)
        assertEqual(3, (v2[3].value!! as Array<*>).size)
    }

    fun testParseObject() {
        val v1 = input("{\"1\":1,\"2\":2,\"3\":3}").value!! as Map<String, JSONType<*>>
        assertEqual(true, v1.containsKey("1"))
        assertEqual(1.0, v1.get("1")?.value as Double)
        assertEqual(true, v1.containsKey("2"))
        assertEqual(2.0, v1.get("2")?.value as Double)
        assertEqual(true, v1.containsKey("3"))
        assertEqual(3.0, v1.get("3")?.value as Double)
    }

    private fun input(json: String): JSONType<*> {
        return jsonParse.parse(JSONInput(json))
    }
}