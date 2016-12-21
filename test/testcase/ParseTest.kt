package testcase

import json.JSONType
import json.Parse
import test.assertEqual

class ParseTest {
    fun testParseNull() {
        val p = Parse("null")
        assertEqual(JSONType.NULL, p.parse())
    }

    fun testParseFalse() {
        val p = Parse("false")
        assertEqual(JSONType.FALSE, p.parse())
    }

    fun testParseTrue() {
        val p = Parse("true")
        assertEqual(JSONType.TRUE, p.parse().name)
    }
}