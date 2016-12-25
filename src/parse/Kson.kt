package parse

import java.util.*

class Kson {

    fun parse(input: String): Any? {
        val pureInput = input.trim()
        return parseValue(pureInput)
    }


    private fun parseValue(input: String): Any? {
        when (input[0]) {
            'n' -> return parseNull(input)
            'f' -> return parseBoolean(input)
            't' -> return parseBoolean(input)
            '"' -> return parseString(input)
            '[' -> return parseArray(input)
            '{' -> return parseObject(input)
            else -> return parseNumber(input)
        }
    }

    private fun parseNull(input: String): Any? {
        if (input == "null") {
            return null
        }
        throw IllegalArgumentException("invalid value, not a null: " + input)
    }

    private fun parseBoolean(input: String): Boolean {
        try {
            return input.toBoolean()
        } catch (e: Exception) {
            throw IllegalArgumentException("invalid value, not a boolean: " + input)
        }
    }

    private fun parseNumber(input: String): Double {
        try {
            return input.toDouble()
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("invalid value, not a number: " + input)
        }
    }

    private fun parseString(input: String): String {
        if (input.length > 1 && input[0] == '"' && input[input.length - 1] == '"') {
            return input.substring(1, input.length - 1)
        }
        throw IllegalArgumentException("invalid value, not a string: " + input)
    }

    private fun parseArray(input: String): ArrayList<*> {
        if (input.length < 2 || input[0] != '[' || input[input.length - 1] != ']')
            throw IllegalArgumentException("invalid value, not a array: " + input)

        if (input.substring(1, input.length - 1).isBlank())
            return ArrayList<Any?>()

        // left parenthesis count
        var lpc = 1
        val values = ArrayList<Any?>()

        var start = 1
        var index = 1
        while (lpc != 0) {
            if (index == input.length)
                throw IllegalArgumentException("invalid value, lack ]")

            val c = input[index++]
            if (c == '[') {
                lpc++
                continue
            }
            if (c == ']') {
                lpc--
                if (lpc == 0) {
                    val o = parse(input.substring(start, index - 1))
                    values.add(o)
                }
                continue
            }
            if (c == ',' && lpc == 1) {
                val o = parse(input.substring(start, index - 1))
                values.add(o)
                start = index
                continue
            }
        }

        if (index != input.length)
            throw IllegalArgumentException("root not single")
        return values
    }

    private fun parseObject(input: String): Map<String, *> {
        if (input.length < 2 || input[0] != '{' || input[input.length - 1] != '}')
            throw IllegalArgumentException("invalid value, not a object: " + input)

        if (input.substring(1, input.length - 1).isBlank())
            return HashMap<String, Any>()

        // left parenthesis count
        var lpc = 1
        val values = HashMap<String, Any?>()

        var start = 1
        var index = 1
        while (lpc != 0) {
            if (index == input.length)
                throw IllegalArgumentException("invalid value, lack }")

            val c = input[index++]
            if (c == '{') {
                lpc++
                continue
            }
            if (c == '}') {
                lpc--
                if (lpc == 0) {
                    val (k, v) = parseField(input.substring(start, index - 1))
                    values.put(k, v)
                }
                continue
            }
            if (c == ',' && lpc == 1) {
                val (k, v) = parseField(input.substring(start, index - 1))
                values.put(k, v)
                start = index
                continue
            }
        }

        if (index != input.length)
            throw IllegalArgumentException("root not single")
        return values
    }

    private fun parseField(input: String): Pair<String, *> {
        val index = input.indexOf(':')

        val key = parse(input.substring(0, index))
        if (!(key is String))
            throw  IllegalArgumentException("invalid key: " + input.substring(0, index))

        val value = parse(input.substring(index + 1, input.length))

        return Pair(key, value)
    }
}