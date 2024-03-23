package com.dladukedev.common.models

typealias ReadingSet = List<Reading>

sealed class Reading {
    abstract val book: BibleBook

    data class SingleChapter(override val book: BibleBook, val chapter: String) : Reading()

    data class PartialChapter(
        override val book: BibleBook,
        val chapter: String,
        val verseStart: String,
        val verseEnd: String
    ) :
        Reading()

    data class MultipleChapters(
        override val book: BibleBook,
        val chapterStart: String,
        val chapterEnd: String
    ) : Reading()

    data class PartialChapters(
        override val book: BibleBook,
        val chapterStart: String,
        val verseStart: String,
        val chapterEnd: String,
        val verseEnd: String,
    ) : Reading()

    data class WholeBook(override val book: BibleBook) : Reading()
}