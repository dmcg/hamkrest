@file:JvmName("Describe")

package com.natpryce.hamkrest

/**
 * Formats [v] to be included in a description.  Strings are delimited with quotes and elements of tuples, ranges,
 * iterable collections and maps are (recursively) described.  A null reference is described as `null`.
 * For anything else, the result of [Any.toString] is used.
 */
fun describe(v: Any?): String = when (v) {
    null -> "null"
    is SelfDescribing -> v.description()
    is String -> "\"" + v.replace("\\", "\\\\").replace("\"", "\\\"") + "\""
    is Pair<*, *> -> Pair(describe(v.first), describe(v.second)).toString()
    is Triple<*, *, *> -> Triple(describe(v.first), describe(v.second), describe(v.third)).toString()
    is ClosedRange<*> -> "${describe(v.start)}..${describe(v.endInclusive)}"
    is Iterable<*> -> v.map(::describe).joinToString(prefix = "[", separator = ", ", postfix = "]")
    is Map<*, *> -> v.entries.map { "${describe(it.key)}:${describe(it.value)}" }.joinToString(prefix = "{", separator = ", ", postfix = "}")
    else -> v.toString()
}

/**
 * An object that can describe itself.
 */
interface SelfDescribing {
    /**
     * Returns the description of this object
     */
    fun description(): String
}

/**
 * Combines a [value] and its [description].
 */
class Described<T>(
        /**
         * The description of [value].
         */
        val description: String,
        /**
         * The value being described.
         */
        val value: T)
    : SelfDescribing
{
    override fun description() = description
}
