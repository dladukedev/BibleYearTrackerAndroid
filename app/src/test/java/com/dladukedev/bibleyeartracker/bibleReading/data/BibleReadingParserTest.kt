package com.dladukedev.bibleyeartracker.bibleReading.data

import com.dladukedev.bibleyeartracker.bibleReading.domain.BibleBook
import com.dladukedev.bibleyeartracker.bibleReading.domain.Reading
import org.junit.Assert
import org.junit.Test

class BibleReadingParserTest {
    private val bibleBookMapper = object : BibleBookMapper {
        override fun mapFromStringToBibleBook(rawBook: String): BibleBook = BibleBook.GENESIS
    }

    @Test
    fun `parse returns SingleChapter for single chapter input`() {
        // Given
        val expected = listOf(Reading.SingleChapter(BibleBook.GENESIS, "1"))
        val bibleReadingParser = BibleReadingsParserImpl(bibleBookMapper)
        val rawReading = "Gn 1"

        // When
        val actual = bibleReadingParser.parse(rawReading)

        // Then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `parse returns PartialChapter for partial chapter input`() {
        // Given
        val expected = listOf(
            Reading.PartialChapter(
                BibleBook.GENESIS,
                "1",
                "1",
                "10"
            )
        )
        val bibleReadingParser = BibleReadingsParserImpl(bibleBookMapper)
        val rawReading = "Gn 1:1-10"

        // When
        val actual = bibleReadingParser.parse(rawReading)

        // Then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `parse returns MultipleChapters for multiple chapter input`() {
        // Given
        val expected = listOf(
            Reading.MultipleChapters(
                BibleBook.GENESIS,
                "1",
                "5",
            )
        )
        val bibleReadingParser = BibleReadingsParserImpl(bibleBookMapper)
        val rawReading = "Gn 1-5"

        // When
        val actual = bibleReadingParser.parse(rawReading)

        // Then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `parse returns PartialChapters for partial chapters input`() {
        // Given
        val expected = listOf(
            Reading.PartialChapters(
                BibleBook.GENESIS,
                "1",
                "1",
                "2",
                "15",
            )
        )
        val bibleReadingParser = BibleReadingsParserImpl(bibleBookMapper)
        val rawReading = "Gn 1:1-2:15"

        // When
        val actual = bibleReadingParser.parse(rawReading)

        // Then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `parse returns WholeBook for whole book input`() {
        // Given
        val expected = listOf(
            Reading.WholeBook(BibleBook.GENESIS)
        )
        val bibleReadingParser = BibleReadingsParserImpl(bibleBookMapper)
        val rawReading = "Gn"

        // When
        val actual = bibleReadingParser.parse(rawReading)

        // Then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `parse returns multiple selections if properly delimited`() {
        // Given
        val expected = listOf(
            Reading.WholeBook(BibleBook.GENESIS),
            Reading.WholeBook(BibleBook.GENESIS),
            Reading.WholeBook(BibleBook.GENESIS),
            Reading.WholeBook(BibleBook.GENESIS),
        )
        val bibleReadingParser = BibleReadingsParserImpl(bibleBookMapper)
        val rawReading = "Gn,Gn,Gn,Gn"

        // When
        val actual = bibleReadingParser.parse(rawReading)

        // Then
        Assert.assertEquals(expected, actual)
    }

}