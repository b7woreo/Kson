package json

import java.util.*

class JSONParse {

    fun parse(input: JSONInput): JSONType<*> {
        parseWhiteSpace(input)
        val type = parseValue(input)
        if (!input.isEmpty())
            parseWhiteSpace(input)
        if (!input.isEmpty()) {
            throw RuntimeException("root not single")
        }
        return type
    }


    private fun parseValue(input: JSONInput): JSONType<*> {
        when (input.peek()) {
            'n' -> return parseNull(input)
            'f' -> return parseFalse(input)
            't' -> return parseTrue(input)
            '"' -> return parseString(input)
            '[' -> return parseArray(input)
            else -> return parseNumber(input)
        }
    }

    private fun parseWhiteSpace(input: JSONInput) {
        while (input.peek() == '\t' || input.peek() == '\n' || input.peek() == '\r' || input.peek() == ' ') {
            input.pop()
            if (input.isEmpty()) break
        }
    }

    private fun parseNull(input: JSONInput): JSONType.Null {
        if (input.pop() == 'n' && input.pop() == 'u' && input.pop() == 'l' && input.pop() == 'l') {
            return JSONType.Null
        }
        throw IllegalArgumentException("invalid value")
    }

    private fun parseFalse(input: JSONInput): JSONType.Boolean {
        if (input.pop() == 'f' && input.pop() == 'a' && input.pop() == 'l'
                && input.pop() == 's' && input.pop() == 'e') {
            return JSONType.Boolean(false)
        }
        throw IllegalArgumentException("invalid value")
    }

    private fun parseTrue(input: JSONInput): JSONType.Boolean {
        if (input.pop() == 't' && input.pop() == 'r' && input.pop() == 'u' && input.pop() == 'e') {
            return JSONType.Boolean(true)
        }
        throw IllegalArgumentException("invalid value")
    }

    private fun parseNumber(input: JSONInput): JSONType.Number {
        val builder = StringBuilder()
        var c = input.peek()
        while (('0' <= c && c <= '9') || c == '.' || c == '-') {
            builder.append(input.pop())
            if (input.isEmpty()) break
            c = input.peek()
        }
        try {
            val n = builder.toString().toDouble()
            return JSONType.Number(n)
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("invalid value")
        }
    }

    private fun parseString(input: JSONInput): JSONType.String {
        val builder = StringBuilder()
        input.pop()
        var c = input.peek()
        while (c != '"') {
            builder.append(input.pop())
            c = input.peek()
        }
        input.pop()
        return JSONType.String(builder.toString())
    }

    private fun parseArray(input: JSONInput): JSONType.Array {
        assert(input.peek() == '[')
        val stack = ArrayDeque<Char>()
        stack.push(input.pop())
        val values = ArrayList<JSONType<*>>()
        var builder = StringBuilder()

        while (!stack.isEmpty()) {
            if (input.isEmpty()) throw IllegalArgumentException("invalid value")

            val c = input.pop()
            if (c == '[') {
                stack.push(c)
                builder.append(c)
            } else if (c == ']') {
                stack.pop()
                if (stack.isEmpty()) {
                    if (builder.isBlank()) {
                        return JSONType.Array(Array<JSONType<*>>(0, { JSONType.Null }))
                    }
                    val type = parse(JSONInput(builder.toString()))
                    values.add(type)
                } else {
                    builder.append(']')
                }
            } else if (c == ',' && stack.size == 1) {
                val type = parse(JSONInput(builder.toString()))
                values.add(type)
                builder = StringBuilder()
            } else {
                builder.append(c)
            }
        }

        return JSONType.Array(values.toArray(Array<JSONType<*>>(values.size, { JSONType.Null })))
    }
}