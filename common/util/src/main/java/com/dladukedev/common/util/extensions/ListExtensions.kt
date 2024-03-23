package com.dladukedev.common.util.extensions

fun <T> List<T>.headTail(): Pair<T, List<T>> {
    require(this.isNotEmpty())

    val head = this.first()
    val tail = this.drop(1)

    return Pair(head, tail)
}

fun <T> List<T>.splitAt(index: Int, afterIndex: Boolean = true): Pair<List<T>, List<T>> {
    val count = if(afterIndex) index + 1 else index

    if(count < 0) { throw IndexOutOfBoundsException() }
    if(count > this.lastIndex) { throw IndexOutOfBoundsException() }

    val first = this.take(count)
    val second = this.drop(count)

    return Pair(first, second)
}