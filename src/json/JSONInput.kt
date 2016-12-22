package json

class JSONInput(input: String) {
    private val json = input
    private var index = 0

    fun peek(): Char {
        return json[index]
    }

    fun next(): Char {
        return json[index++]
    }

    fun hasNext(): Boolean {
        return index == json.length
    }
}