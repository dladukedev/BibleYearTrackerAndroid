package com.dladukedev.common.util.extensions

import org.junit.Assert
import org.junit.Test
import java.lang.IllegalArgumentException

class ListExtensionsTest {
   @Test
   fun `headTail returns the first list item in first and the remaining items in second`() {
       // Given
       val list = listOf(1,2,3,4,5)
       val expected = Pair(1, listOf(2,3,4,5))

       // When
       val actual = list.headTail()

       // Then
       Assert.assertEquals(expected, actual)
   }

    @Test
    fun `headTail returns empty list for second when list contains single item`() {
        // Given
        val list = listOf(1)
        val expected = Pair(1, emptyList<Int>())

        // When
        val actual = list.headTail()

        // Then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `headTail throws an exception when list is empty`() {
        // Given
        val list = emptyList<Int>()

        // Then
        Assert.assertThrows(IllegalArgumentException::class.java) {
           // When
           list.headTail()
        }
    }
}