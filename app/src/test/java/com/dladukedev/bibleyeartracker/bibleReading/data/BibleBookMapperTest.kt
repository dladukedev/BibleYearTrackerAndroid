package com.dladukedev.bibleyeartracker.bibleReading.data

import com.dladukedev.bibleyeartracker.bibleReading.domain.BibleBook
import org.junit.Assert
import org.junit.Test
import java.lang.IllegalArgumentException


// BibleReadings - Display
// BibleReadingScreenState?

// Settings - Display
//// SettingsScreenStateHolder?



// Settings - Display
// SettingsScreenViewModel


class BibleBookMapperTest {
    @Test
    fun `mapFromStringToBibleBook maps values correctly`() {
        // Given
        val bibleBookMapper = BibleBookMapperImpl()

        // When
        val genesis = bibleBookMapper.mapFromStringToBibleBook("Gn")
        val exodus = bibleBookMapper.mapFromStringToBibleBook("Ex")
        val leviticus = bibleBookMapper.mapFromStringToBibleBook("Lv")
        val numbers = bibleBookMapper.mapFromStringToBibleBook("Nm")
        val deuteronomy = bibleBookMapper.mapFromStringToBibleBook("Dt")
        val joshua = bibleBookMapper.mapFromStringToBibleBook("Jos")
        val judges = bibleBookMapper.mapFromStringToBibleBook("Jgs")
        val ruth = bibleBookMapper.mapFromStringToBibleBook("Ruth")
        val firstSamuel = bibleBookMapper.mapFromStringToBibleBook("1 Sm")
        val secondSamuel = bibleBookMapper.mapFromStringToBibleBook("2 Sm")
        val firstKings = bibleBookMapper.mapFromStringToBibleBook("1 Kgs")
        val secondKings = bibleBookMapper.mapFromStringToBibleBook("2 Kgs")
        val firstChronicles = bibleBookMapper.mapFromStringToBibleBook("1 Chr")
        val secondChronicles = bibleBookMapper.mapFromStringToBibleBook("2 Chr")
        val ezra = bibleBookMapper.mapFromStringToBibleBook("Ezra")
        val nehemiah = bibleBookMapper.mapFromStringToBibleBook("Neh")
        val tobit = bibleBookMapper.mapFromStringToBibleBook("Tb")
        val judith = bibleBookMapper.mapFromStringToBibleBook("Jdt")
        val esther = bibleBookMapper.mapFromStringToBibleBook("Es")
        val firstMaccabees = bibleBookMapper.mapFromStringToBibleBook("1 Mac")
        val secondMaccabees = bibleBookMapper.mapFromStringToBibleBook("2 Mac")
        val job = bibleBookMapper.mapFromStringToBibleBook("Jb")
        val psalms = bibleBookMapper.mapFromStringToBibleBook("Ps")
        val proverbs = bibleBookMapper.mapFromStringToBibleBook("Prv")
        val ecclesiastes = bibleBookMapper.mapFromStringToBibleBook("Ecc")
        val songOfSongs = bibleBookMapper.mapFromStringToBibleBook("Sgs")
        val wisdom = bibleBookMapper.mapFromStringToBibleBook("Wis")
        val sirach = bibleBookMapper.mapFromStringToBibleBook("Sir")
        val isaiah = bibleBookMapper.mapFromStringToBibleBook("Is")
        val jeremiah = bibleBookMapper.mapFromStringToBibleBook("Jer")
        val lamentations = bibleBookMapper.mapFromStringToBibleBook("Lam")
        val baruch = bibleBookMapper.mapFromStringToBibleBook("Bar")
        val ezekiel = bibleBookMapper.mapFromStringToBibleBook("Ez")
        val daniel = bibleBookMapper.mapFromStringToBibleBook("Dn")
        val hosea = bibleBookMapper.mapFromStringToBibleBook("Hos")
        val joel = bibleBookMapper.mapFromStringToBibleBook("Joel")
        val amos = bibleBookMapper.mapFromStringToBibleBook("Am")
        val obadiah = bibleBookMapper.mapFromStringToBibleBook("Obadiah")
        val jonah = bibleBookMapper.mapFromStringToBibleBook("Jon")
        val micah = bibleBookMapper.mapFromStringToBibleBook("Mic")
        val nahum = bibleBookMapper.mapFromStringToBibleBook("Nahum")
        val habakkuk = bibleBookMapper.mapFromStringToBibleBook("Habakkuk")
        val zephaniah = bibleBookMapper.mapFromStringToBibleBook("Zephaniah")
        val haggai = bibleBookMapper.mapFromStringToBibleBook("Haggai")
        val zechariah = bibleBookMapper.mapFromStringToBibleBook("Zech")
        val malachi = bibleBookMapper.mapFromStringToBibleBook("Malachi")
        val matthew = bibleBookMapper.mapFromStringToBibleBook("Mt")
        val mark = bibleBookMapper.mapFromStringToBibleBook("Mk")
        val luke = bibleBookMapper.mapFromStringToBibleBook("Lk")
        val john = bibleBookMapper.mapFromStringToBibleBook("Jn")
        val acts = bibleBookMapper.mapFromStringToBibleBook("Acts")
        val romans = bibleBookMapper.mapFromStringToBibleBook("Rom")
        val firstCorinthians = bibleBookMapper.mapFromStringToBibleBook("1 Cor")
        val secondCorinthians = bibleBookMapper.mapFromStringToBibleBook("2 Cor")
        val galatians = bibleBookMapper.mapFromStringToBibleBook("Gal")
        val ephesians = bibleBookMapper.mapFromStringToBibleBook("Eph")
        val philippians = bibleBookMapper.mapFromStringToBibleBook("Phil")
        val colossians = bibleBookMapper.mapFromStringToBibleBook("Col")
        val firstThessalonians = bibleBookMapper.mapFromStringToBibleBook("1 Thes")
        val secondThessalonians = bibleBookMapper.mapFromStringToBibleBook("2 Thes")
        val firstTimothy = bibleBookMapper.mapFromStringToBibleBook("1 Tim")
        val secondTimothy = bibleBookMapper.mapFromStringToBibleBook("2 Tim")
        val titus = bibleBookMapper.mapFromStringToBibleBook("Titus")
        val philemon = bibleBookMapper.mapFromStringToBibleBook("Philemon")
        val hebrews = bibleBookMapper.mapFromStringToBibleBook("Heb")
        val james = bibleBookMapper.mapFromStringToBibleBook("Jas")
        val firstPeter = bibleBookMapper.mapFromStringToBibleBook("1 Pt")
        val secondPeter = bibleBookMapper.mapFromStringToBibleBook("2 Pt")
        val firstJohn = bibleBookMapper.mapFromStringToBibleBook("1 Jn")
        val secondJohn = bibleBookMapper.mapFromStringToBibleBook("2 Jn")
        val thirdJohn = bibleBookMapper.mapFromStringToBibleBook("3 Jn")
        val jude = bibleBookMapper.mapFromStringToBibleBook("Jude")
        val revelation = bibleBookMapper.mapFromStringToBibleBook("Rev")

        // Then
        Assert.assertEquals(genesis, BibleBook.GENESIS)
        Assert.assertEquals(exodus, BibleBook.EXODUS)
        Assert.assertEquals(leviticus, BibleBook.LEVITICUS)
        Assert.assertEquals(numbers, BibleBook.NUMBERS)
        Assert.assertEquals(deuteronomy, BibleBook.DEUTERONOMY)
        Assert.assertEquals(joshua, BibleBook.JOSHUA)
        Assert.assertEquals(judges, BibleBook.JUDGES)
        Assert.assertEquals(ruth, BibleBook.RUTH)
        Assert.assertEquals(firstSamuel, BibleBook.FIRST_SAMUEL)
        Assert.assertEquals(secondSamuel, BibleBook.SECOND_SAMUEL)
        Assert.assertEquals(firstKings, BibleBook.FIRST_KINGS)
        Assert.assertEquals(secondKings, BibleBook.SECOND_KINGS)
        Assert.assertEquals(firstChronicles, BibleBook.FIRST_CHRONICLES)
        Assert.assertEquals(secondChronicles, BibleBook.SECOND_CHRONICLES)
        Assert.assertEquals(ezra, BibleBook.EZRA)
        Assert.assertEquals(nehemiah, BibleBook.NEHEMIAH)
        Assert.assertEquals(tobit, BibleBook.TOBIT)
        Assert.assertEquals(judith, BibleBook.JUDITH)
        Assert.assertEquals(esther, BibleBook.ESTHER)
        Assert.assertEquals(firstMaccabees, BibleBook.FIRST_MACCABEES)
        Assert.assertEquals(secondMaccabees, BibleBook.SECOND_MACCABEES)
        Assert.assertEquals(job, BibleBook.JOB)
        Assert.assertEquals(psalms, BibleBook.PSALMS)
        Assert.assertEquals(proverbs, BibleBook.PROVERBS)
        Assert.assertEquals(ecclesiastes, BibleBook.ECCLESIASTES)
        Assert.assertEquals(songOfSongs, BibleBook.SONG_OF_SONGS)
        Assert.assertEquals(wisdom, BibleBook.WISDOM)
        Assert.assertEquals(sirach, BibleBook.SIRACH)
        Assert.assertEquals(isaiah, BibleBook.ISAIAH)
        Assert.assertEquals(jeremiah, BibleBook.JEREMIAH)
        Assert.assertEquals(lamentations, BibleBook.LAMENTATIONS)
        Assert.assertEquals(baruch, BibleBook.BARUCH)
        Assert.assertEquals(ezekiel, BibleBook.EZEKIEL)
        Assert.assertEquals(daniel, BibleBook.DANIEL)
        Assert.assertEquals(hosea, BibleBook.HOSEA)
        Assert.assertEquals(joel, BibleBook.JOEL)
        Assert.assertEquals(amos, BibleBook.AMOS)
        Assert.assertEquals(obadiah, BibleBook.OBADIAH)
        Assert.assertEquals(jonah, BibleBook.JONAH)
        Assert.assertEquals(micah, BibleBook.MICAH)
        Assert.assertEquals(nahum, BibleBook.NAHUM)
        Assert.assertEquals(habakkuk, BibleBook.HABAKKUK)
        Assert.assertEquals(zephaniah, BibleBook.ZEPHANIAH)
        Assert.assertEquals(haggai, BibleBook.HAGGAI)
        Assert.assertEquals(zechariah, BibleBook.ZECHARIAH)
        Assert.assertEquals(malachi, BibleBook.MALACHI)
        Assert.assertEquals(matthew, BibleBook.MATTHEW)
        Assert.assertEquals(mark, BibleBook.MARK)
        Assert.assertEquals(luke, BibleBook.LUKE)
        Assert.assertEquals(john, BibleBook.JOHN)
        Assert.assertEquals(acts, BibleBook.ACTS)
        Assert.assertEquals(romans, BibleBook.ROMANS)
        Assert.assertEquals(firstCorinthians, BibleBook.FIRST_CORINTHIANS)
        Assert.assertEquals(secondCorinthians, BibleBook.SECOND_CORINTHIANS)
        Assert.assertEquals(galatians, BibleBook.GALATIANS)
        Assert.assertEquals(ephesians, BibleBook.EPHESIANS)
        Assert.assertEquals(philippians, BibleBook.PHILIPPIANS)
        Assert.assertEquals(colossians, BibleBook.COLOSSIANS)
        Assert.assertEquals(firstThessalonians, BibleBook.FIRST_THESSALONIANS)
        Assert.assertEquals(secondThessalonians, BibleBook.SECOND_THESSALONIANS)
        Assert.assertEquals(firstTimothy, BibleBook.FIRST_TIMOTHY)
        Assert.assertEquals(secondTimothy, BibleBook.SECOND_TIMOTHY)
        Assert.assertEquals(titus, BibleBook.TITUS)
        Assert.assertEquals(philemon, BibleBook.PHILEMON)
        Assert.assertEquals(hebrews, BibleBook.HEBREWS)
        Assert.assertEquals(james, BibleBook.JAMES)
        Assert.assertEquals(firstPeter, BibleBook.FIRST_PETER)
        Assert.assertEquals(secondPeter, BibleBook.SECOND_PETER)
        Assert.assertEquals(firstJohn, BibleBook.FIRST_JOHN)
        Assert.assertEquals(secondJohn, BibleBook.SECOND_JOHN)
        Assert.assertEquals(thirdJohn, BibleBook.THIRD_JOHN)
        Assert.assertEquals(jude, BibleBook.JUDE)
        Assert.assertEquals(revelation, BibleBook.REVELATION)
    }

    @Test
    fun `mapFromStringToBibleBook throws IllegalArgumentException for invalid value`() {
        // Given
        val bibleBookMapper = BibleBookMapperImpl()

        // Then
        Assert.assertThrows(IllegalArgumentException::class.java) {
            // When
            bibleBookMapper.mapFromStringToBibleBook("")
        }
    }

}

