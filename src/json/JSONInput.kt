package json

class JSONInput(input: String) {
    private val json = input
    private var index = 0

    fun peek(): Char {
        return json[index]
    }

    fun pop(): Char {
        return json[index++]
    }

    fun isEmpty(): Boolean {
        return index == json.length
    }
}