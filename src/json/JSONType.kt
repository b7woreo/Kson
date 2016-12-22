package json

sealed class JSONType<out T>(val value: T) {
    object Null : JSONType<Any>(Any())

    class Boolean(value: kotlin.Boolean) : JSONType<kotlin.Boolean>(value)

    class Number(value: Double) : JSONType<Double>(value)

    class String(value: kotlin.String) : JSONType<kotlin.String>(value)

    class Array(value: kotlin.Array<JSONType<*>>) : JSONType<kotlin.Array<JSONType<*>>>(value)

    class Object(value: Map<kotlin.String, JSONType<*>>) : JSONType<Map<kotlin.String, JSONType<*>>>(value)
}

