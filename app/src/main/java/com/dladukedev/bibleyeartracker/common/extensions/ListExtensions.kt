package com.dladukedev.bibleyeartracker.common.extensions

fun <T> List<T>.headTail(): Pair<T, List<T>> {
    require(this.isNotEmpty())

    val head = this.first()
    val tail = this.drop(1)

    return Pair(head, tail)
}