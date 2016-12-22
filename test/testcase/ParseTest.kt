package testcase

import json.JSONInput
import json.JSONParse
import json.JSONType
import test.assertEqual

class ParseTest {
    fun testParseNull() {
        val p = JSONParse(JSONInput("null"))
        assertEqual(JSONType.Null, p.parse())
    }

    fun testParseFalse() {
        val p = JSONParse(JSONInput("false"))
        assertEqual(JSONType.Boolean(false).value, p.parse().value!!)
    }

    fun testParseTrue() {
        val p = JSONParse(JSONInput("true"))
        assertEqual(JSONType.Boolean(true).value, p.parse().value!!)
    }

    fun testParseNumber() {
        val p = JSONParse(JSONInput("123"))
        assertEqual(JSONType.Number(123.0).value, p.parse().value!!)
    }
}