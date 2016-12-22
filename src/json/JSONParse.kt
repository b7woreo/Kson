package json

class JSONParse(val json: JSONInput) {

    fun parse(): JSONType<*> {
        parseWhiteSpace()
        val type = parseValue()
        if (!json.hasNext())
            parseWhiteSpace()
        if (!json.hasNext()) {
            throw RuntimeException("root not single")
        }
        return type
    }


    fun parseValue(): JSONType<*> {
        when (json.peek()) {
            'n' -> {
                return parseNull()
            }
            'f' -> {
                return parseFalse()
            }
            't' -> {
                return parseTrue()
            }
            else -> throw RuntimeException("invalid value")
        }
    }

    fun parseWhiteSpace() {
        while (json.peek() == '\t' || json.peek() == '\n' || json.peek() == '\r' || json.peek() == ' ')
            json.next()
    }

    fun parseNull(): JSONType.Null {
        if (json.next() == 'n' && json.next() == 'u' && json.next() == 'l' && json.next() == 'l') {
            return JSONType.Null
        }
        throw RuntimeException("invalid value")
    }

    fun parseFalse(): JSONType.Boolean {
        if (json.next() == 'f' && json.next() == 'a' && json.next() == 'l'
                && json.next() == 's' && json.next() == 'e') {
            return JSONType.Boolean(false)
        }
        throw RuntimeException("invalid value")
    }

    fun parseTrue(): JSONType.Boolean {
        if (json.next() == 't' && json.next() == 'r' && json.next() == 'u' && json.next() == 'e') {
            return JSONType.Boolean(true)
        }
        throw RuntimeException("invalid value")
    }
}