package json

class Parse(val json: String) {
    private var index = 0;

    private fun peek(): Char {
        return json[index]
    }

    private fun pop(count: Int) {
        index += count
    }

    private fun isComplete(): Boolean {
        return index == json.length
    }

    fun parse(): Type {
        parseWhiteSpace()
        val type = parseValue()
        if (!isComplete())
            parseWhiteSpace()
        if (!isComplete()) {
            throw RuntimeException("root not single")
        }
        return type
    }


    fun parseValue(): Type {
        when (peek()) {
            'n' -> {
                parseNull()
                return Type.NLLL
            }
            'f' -> {
                parseFalse()
                return Type.FALSE
            }
            't' -> {
                parseTrue()
                return Type.TRUE
            }
            else -> throw RuntimeException("invalid value")
        }
    }

    fun parseWhiteSpace() {
        while (peek() == '\t' || peek() == '\n' || peek() == '\r' || peek() == ' ')
            pop(1)
    }

    fun parseNull() {
        checkEqual("null", 4);
        pop(4)
    }

    fun parseFalse() {
        checkEqual("false", 5)
        pop(5)
    }

    fun parseTrue() {
        checkEqual("true", 5)
        pop(5)
    }

    private fun checkEqual(expect: String, length: Int) {
        if (expect != (json.substring(index, index + length))) {
            throw RuntimeException("invalid value")
        }
    }
}