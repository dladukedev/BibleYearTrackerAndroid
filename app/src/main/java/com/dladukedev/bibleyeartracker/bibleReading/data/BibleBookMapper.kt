package com.dladukedev.bibleyeartracker.bibleReading.data

import com.dladukedev.bibleyeartracker.bibleReading.domain.BibleBook
import javax.inject.Inject

interface BibleBookMapper {
    fun mapFromStringToBibleBook(rawBook: String): BibleBook
}

class BibleBookMapperImpl @Inject constructor() : BibleBookMapper {
    override fun mapFromStringToBibleBook(rawBook: String): BibleBook {
        return stringToBibleBookMap[rawBook.trim()]
            ?: throw IllegalArgumentException("No value in map for book key: $rawBook")
    }

    private val stringToBibleBookMap = mapOf(
        Pair("Gn", BibleBook.GENESIS),
        Pair("Ex", BibleBook.EXODUS),
        Pair("Lv", BibleBook.LEVITICUS),
        Pair("Nm", BibleBook.NUMBERS),
        Pair("Dt", BibleBook.DEUTERONOMY),
        Pair("Jos", BibleBook.JOSHUA),
        Pair("Jgs", BibleBook.JUDGES),
        Pair("Ruth", BibleBook.RUTH),
        Pair("1 Sm", BibleBook.FIRST_SAMUEL),
        Pair("2 Sm", BibleBook.SECOND_SAMUEL),
        Pair("1 Kgs", BibleBook.FIRST_KINGS),
        Pair("2 Kgs", BibleBook.SECOND_KINGS),
        Pair("1 Chr", BibleBook.FIRST_CHRONICLES),
        Pair("2 Chr", BibleBook.SECOND_CHRONICLES),
        Pair("Ezra", BibleBook.EZRA),
        Pair("Neh", BibleBook.NEHEMIAH),
        Pair("Tb", BibleBook.TOBIT),
        Pair("Jdt", BibleBook.JUDITH),
        Pair("Es", BibleBook.ESTHER),
        Pair("1 Mac", BibleBook.FIRST_MACCABEES),
        Pair("2 Mac", BibleBook.SECOND_MACCABEES),
        Pair("Jb", BibleBook.JOB),
        Pair("Ps", BibleBook.PSALMS),
        Pair("Prv", BibleBook.PROVERBS),
        Pair("Ecc", BibleBook.ECCLESIASTES),
        Pair("Sgs", BibleBook.SONG_OF_SONGS),
        Pair("Wis", BibleBook.WISDOM),
        Pair("Sir", BibleBook.SIRACH),
        Pair("Is", BibleBook.ISAIAH),
        Pair("Jer", BibleBook.JEREMIAH),
        Pair("Lam", BibleBook.LAMENTATIONS),
        Pair("Bar", BibleBook.BARUCH),
        Pair("Ez", BibleBook.EZEKIEL),
        Pair("Dn", BibleBook.DANIEL),
        Pair("Hos", BibleBook.HOSEA),
        Pair("Joel", BibleBook.JOEL),
        Pair("Am", BibleBook.AMOS),
        Pair("Obadiah", BibleBook.OBADIAH),
        Pair("Jon", BibleBook.JONAH),
        Pair("Mic", BibleBook.MICAH),
        Pair("Nahum", BibleBook.NAHUM),
        Pair("Habakkuk", BibleBook.HABAKKUK),
        Pair("Zephaniah", BibleBook.ZEPHANIAH),
        Pair("Haggai", BibleBook.HAGGAI),
        Pair("Zech", BibleBook.ZECHARIAH),
        Pair("Malachi", BibleBook.MALACHI),

        Pair("Mt", BibleBook.MATTHEW),
        Pair("Mk", BibleBook.MARK),
        Pair("Lk", BibleBook.LUKE),
        Pair("Jn", BibleBook.JOHN),
        Pair("Acts", BibleBook.ACTS),
        Pair("Rom", BibleBook.ROMANS),
        Pair("1 Cor", BibleBook.FIRST_CORINTHIANS),
        Pair("2 Cor", BibleBook.SECOND_CORINTHIANS),
        Pair("Gal", BibleBook.GALATIANS),
        Pair("Eph", BibleBook.EPHESIANS),
        Pair("Phil", BibleBook.PHILIPPIANS),
        Pair("Col", BibleBook.COLOSSIANS),
        Pair("1 Thes", BibleBook.FIRST_THESSALONIANS),
        Pair("2 Thes", BibleBook.SECOND_THESSALONIANS),
        Pair("1 Tim", BibleBook.FIRST_TIMOTHY),
        Pair("2 Tim", BibleBook.SECOND_TIMOTHY),
        Pair("Titus", BibleBook.TITUS),
        Pair("Philemon", BibleBook.PHILEMON),
        Pair("Heb", BibleBook.HEBREWS),
        Pair("Jas", BibleBook.JAMES),
        Pair("1 Pt", BibleBook.FIRST_PETER),
        Pair("2 Pt", BibleBook.SECOND_PETER),
        Pair("1 Jn", BibleBook.FIRST_JOHN),
        Pair("2 Jn", BibleBook.SECOND_JOHN),
        Pair("3 Jn", BibleBook.THIRD_JOHN),
        Pair("Jude", BibleBook.JUDE),
        Pair("Rev", BibleBook.REVELATION),
    )
}
