package nicestring

import java.util.*

fun String.isNice(): Boolean {
    var result = 0
    if (checkOne(this)) result++
    if (checkTwo(this)) result++
    if (checkThree(this)) result++

    return result >= 2
}

fun checkOne(item: String): Boolean {
    return !item.contains("bu") && !item.contains("ba") && !item.contains("be")
}

fun checkTwo(item: String): Boolean {
    return item.count { listOf("a", "e", "i", "o", "u").contains(it.toString()) } >= 3
}

fun checkThree(item: String): Boolean {
    var current = 4000
    var result = false
    item.chars().forEach { if (it == current) result = true else current = it }

    return result
}