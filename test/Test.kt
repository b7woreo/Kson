package test

import json.Parse

fun main(args: Array<String>) {
    val p = Parse("null")
    val t = p.parse()
    print(t.name)
}