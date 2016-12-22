package json

class JSONParse(val json: JSONInput) {

    fun parse(): JSONType<*> {
        parseWhiteSpace()
        val type = parseValue()
        if (json.hasNext())
            parseWhiteSpace()
        if (json.hasNext()) {
            throw RuntimeException("root not single")
        }
        return type
    }


    private fun parseValue(): JSONType<*> {
        when (json.peek()) {
            'n' -> return parseNull()
            'f' -> return parseFalse()
            't' -> return parseTrue()
            '"' -> return parseString()
            else -> return parseNumber()
        }
    }

    private fun parseWhiteSpace() {
        while (json.peek() == '\t' || json.peek() == '\n' || json.peek() == '\r' || json.peek() == ' ') {
            json.next()
            if (!json.hasNext()) break
        }
    }

    private fun parseNull(): JSONType.Null {
        if (json.next() == 'n' && json.next() == 'u' && json.next() == 'l' && json.next() == 'l') {
            return JSONType.Null
        }
        throw RuntimeException("invalid value")
    }

    private fun parseFalse(): JSONType.Boolean {
        if (json.next() == 'f' && json.next() == 'a' && json.next() == 'l'
                && json.next() == 's' && json.next() == 'e') {
            return JSONType.Boolean(false)
        }
        throw RuntimeException("invalid value")
    }

    private fun parseTrue(): JSONType.Boolean {
        if (json.next() == 't' && json.next() == 'r' && json.next() == 'u' && json.next() == 'e') {
            return JSONType.Boolean(true)
        }
        throw RuntimeException("invalid value")
    }

    private fun parseNumber(): JSONType.Number {
        val builder = StringBuilder()
        var c = json.peek()
        while (('0' <= c && c <= '9') || c == '.' || c == '-') {
            builder.append(json.next())
            if (!json.hasNext()) break
            c = json.peek()
        }
        val n = builder.toString().toDouble()
        return JSONType.Number(n)
    }

    private fun parseString(): JSONType.String {
        val builder = StringBuilder()
        json.next()
        var c = json.peek()
        while (c != '"') {
            builder.append(json.next())
            c = json.peek()
        }
        json.next()
        return JSONType.String(builder.toString())
    }
}