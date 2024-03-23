package com.dladukedev.data.plan.parseplan

import com.dladukedev.common.models.Reading
import javax.inject.Inject

interface BibleReadingParser {
    fun parse(rawReading: String): List<Reading>
}

class BibleReadingsParserImpl @Inject constructor(
    private val bibleBookMapper: BibleBookMapper,
) : BibleReadingParser {
    override fun parse(rawReading: String): List<Reading> {
        val rawSelections = rawReading.split(SELECTION_DELIMINATOR)

        val selections = rawSelections.map { rawSelection ->
            parseSelection(rawSelection)
        }

        return selections
    }

    private fun parseSelection(rawSelection: String): Reading {
        // Parsing Order Matters
        return when {
            isPartialChapters(rawSelection) -> parsePartialChapters(rawSelection)
            isPartialChapter(rawSelection) -> parsePartialChapter(rawSelection)
            isMultipleChapters(rawSelection) -> parseMultipleChapters(rawSelection)
            isSingleChapter(rawSelection) -> parseSingleChapter(rawSelection)
            else -> parseWholeBook(rawSelection)
        }
    }

    private fun isSingleChapter(rawReading: String): Boolean {
        return rawReading.last().isDigit()
    }

    private fun isPartialChapters(rawReading: String): Boolean {
        return rawReading.count { it == VERSE_DELIMINATOR } == 2
    }

    private fun isPartialChapter(rawReading: String): Boolean {
        return rawReading.count { it == VERSE_DELIMINATOR } == 1
    }

    private fun isMultipleChapters(rawReading: String): Boolean {
        return rawReading.contains(CHAPTER_DELIMINATOR)
    }

    private fun parseSingleChapter(rawSelection: String): Reading.SingleChapter {
        val (rawBook, rawChaptersVerses) = splitBookAndChapters(rawSelection)
        val book = bibleBookMapper.mapFromStringToBibleBook(rawBook)
        return Reading.SingleChapter(
            book = book,
            chapter = rawChaptersVerses
        )
    }

    private fun parsePartialChapters(rawSelection: String): Reading.PartialChapters {
        val (rawBook, rawChaptersVerses) = splitBookAndChapters(rawSelection)
        val book = bibleBookMapper.mapFromStringToBibleBook(rawBook)

        val (readingStart, readingEnd) = rawChaptersVerses.split(CHAPTER_DELIMINATOR)
        val (chapterStart, verseStart) = readingStart.split(VERSE_DELIMINATOR)
        val (chapterEnd, verseEnd) = readingEnd.split(VERSE_DELIMINATOR)
        return Reading.PartialChapters(
            book = book,
            chapterStart = chapterStart,
            verseStart = verseStart,
            chapterEnd = chapterEnd,
            verseEnd = verseEnd,
        )
    }

    private fun parsePartialChapter(rawSelection: String): Reading.PartialChapter {
        val (rawBook, rawChaptersVerses) = splitBookAndChapters(rawSelection)
        val book = bibleBookMapper.mapFromStringToBibleBook(rawBook)
        val (readingStart, verseEnd) = rawChaptersVerses.split(CHAPTER_DELIMINATOR)
        val (chapter, verseStart) = readingStart.split(VERSE_DELIMINATOR)

        return Reading.PartialChapter(
            book = book,
            chapter = chapter,
            verseStart = verseStart,
            verseEnd = verseEnd,
        )
    }

    private fun parseMultipleChapters(rawSelection: String): Reading.MultipleChapters {
        val (rawBook, rawChaptersVerses) = splitBookAndChapters(rawSelection)
        val book = bibleBookMapper.mapFromStringToBibleBook(rawBook)
        val (chapterStart, chapterEnd) = rawChaptersVerses.split("-")

        return Reading.MultipleChapters(
            book = book,
            chapterStart = chapterStart,
            chapterEnd = chapterEnd,
        )
    }

    private data class BookChapterSplitResult(
        val rawBook: String,
        val rawChaptersVerses: String,
    )

    private fun splitBookAndChapters(rawSelection: String): BookChapterSplitResult {
        val rawBook = rawSelection.substringBeforeLast(BOOK_CHAPTER_DELIMINATOR)
        val rawChaptersVerses = rawSelection.substringAfterLast(BOOK_CHAPTER_DELIMINATOR)

        return BookChapterSplitResult(
            rawBook = rawBook,
            rawChaptersVerses = rawChaptersVerses
        )
    }

    private fun parseWholeBook(rawSelection: String): Reading.WholeBook {
        return Reading.WholeBook(
            book = bibleBookMapper.mapFromStringToBibleBook(rawSelection)
        )
    }

    companion object {
        private const val SELECTION_DELIMINATOR = ','
        private const val CHAPTER_DELIMINATOR = '-'
        private const val VERSE_DELIMINATOR = ':'
        private const val BOOK_CHAPTER_DELIMINATOR = ' '
    }
}